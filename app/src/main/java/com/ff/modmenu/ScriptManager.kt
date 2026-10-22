package com.ff.modmenu

import android.content.Context
import android.os.Environment
import java.io.File

data class ModScript(val name: String, val filePath: String, var isEnabled: Boolean = false, var isBuiltIn: Boolean = false)

object ScriptManager {
    private const val DIR = "AndroidGameModder/scripts"
    fun getDir(ctx: Context): File { val d = File(Environment.getExternalStorageDirectory(), DIR); if (!d.exists()) d.mkdirs(); return d }
    fun list(ctx: Context): List<ModScript> {
        val l = mutableListOf(ModScript("Speed Boost", "built-in", isBuiltIn = true))
        getDir(ctx).listFiles()?.filter { it.extension.equals("lua") }?.sortedByDescending { it.lastModified() }?.forEach { l.add(ModScript(it.nameWithoutExtension, it.absolutePath)) }
        return l
    }
    fun save(ctx: Context, name: String, content: String): File { val f = File(getDir(ctx), "${name.replace(Regex("[^a-zA-Z0-9_-]"),"_")}.lua"); f.writeText(content); return f }
    fun delete(path: String) = try { File(path).delete() } catch (_: Exception) { false }
    fun read(path: String) = File(path).readText()
}
