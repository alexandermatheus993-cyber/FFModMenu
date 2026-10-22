package com.ff.modmenu

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object ScriptDownloader {
    private val client = OkHttpClient.Builder().connectTimeout(15,TimeUnit.SECONDS).readTimeout(30,TimeUnit.SECONDS).followRedirects(true).build()
    fun download(ctx: Context, url: String, cb: (Boolean,String)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val r = client.newCall(Request.Builder().url(url).build()).execute()
                if (!r.isSuccessful) { withContext(Dispatchers.Main){cb(false,"HTTP ${r.code}")}; return@launch }
                val body = r.body?.string() ?: ""
                val name = url.substringAfterLast("/").substringBefore(".").ifEmpty{"downloaded"}
                val f = ScriptManager.save(ctx, name, body)
                withContext(Dispatchers.Main) { Toast.makeText(ctx,"Baixado: ${f.name}",Toast.LENGTH_SHORT).show(); cb(true,f.absolutePath) }
            } catch (e: Exception) { withContext(Dispatchers.Main){cb(false,"Erro: ${e.message}")} }
        }
    }
}
