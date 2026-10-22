package com.ff.modmenu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScriptsActivity : AppCompatActivity() {
    private lateinit var adapter: ScriptAdapter
    private val items = mutableListOf<ModScript>()
    override fun onCreate(b: Bundle?) {
        super.onCreate(b); setContentView(R.layout.activity_scripts)
        val rv = findViewById<RecyclerView>(R.id.rv_scripts)
        val et = findViewById<EditText>(R.id.et_script_url)
        adapter = ScriptAdapter(items, { run(it) }, { s,on -> s.isEnabled = on; if (on) run(it) }, { s -> ScriptManager.delete(s.filePath); load() })
        rv.layoutManager = LinearLayoutManager(this); rv.adapter = adapter; load()
        findViewById<Button>(R.id.btn_download).setOnClickListener { val url = et.text.toString().trim(); if (!url.startsWith("http")) { Toast.makeText(this,"URL invalida",Toast.LENGTH_SHORT).show(); return@setOnClickListener }; ScriptDownloader.download(this,url) { ok,_ -> if (ok) { et.text.clear(); load() } } }
        findViewById<Button>(R.id.btn_select_file).setOnClickListener { startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply { addCategory(Intent.CATEGORY_OPENABLE); type = "*/*" },2001) }
    }
    private fun load() { items.clear(); items.addAll(ScriptManager.list(this)); adapter.notifyDataSetChanged() }
    private fun run(s: ModScript) {
        val c = try { if (s.isBuiltIn) assets.open("example_speed.lua").bufferedReader().readText() else ScriptManager.read(s.filePath) } catch (e: Exception) { Toast.makeText(this,"Erro: ${e.message}",Toast.LENGTH_SHORT).show(); return }
        when (CheatController.mode) { CheatController.Mode.GAMEGUARDIAN -> GameGuardianBridge.executeScript(this,c); CheatController.Mode.SHIZUKU -> Toast.makeText(this,ShizukuBridge.run(c),Toast.LENGTH_LONG).show() }
        Toast.makeText(this,"Rodando: ${s.name}",Toast.LENGTH_SHORT).show()
    }
    override fun onActivityResult(r: Int, c: Int, d: Intent?) {
        super.onActivityResult(r,c,d); if (r == 2001 && c == Activity.RESULT_OK) { d?.data?.let { uri -> try { val t = contentResolver.openInputStream(uri)!!.bufferedReader().readText(); val n = uri.lastPathSegment?.substringAfterLast("/")?.substringBefore(".")?.ifEmpty{"imported"} ?: "imported"; ScriptManager.save(this,n,t); load(); Toast.makeText(this,"Importado: $n",Toast.LENGTH_SHORT).show() } catch (e: Exception) { Toast.makeText(this,"Erro: ${e.message}",Toast.LENGTH_SHORT).show() } } } }
}
cat > ~/FFModMenu/.github/workflows/build.yml << 'EOF'
name: Build APK
on:
  push:
    branches: [ main ]
  workflow_dispatch:
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Wrapper
        run: chmod +x gradlew 2>/dev/null || true
      - name: Build
        run: gradle wrapper --gradle-version 8.2 && ./gradlew assembleDebug
      - name: Upload
        uses: actions/upload-artifact@v4
        with:
          name: FFModMenu-debug
          path: app/build/outputs/apk/debug/app-debug.apk
          retention-days: 90
