package com.ff.modmenu

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import rikka.shizuku.Shizuku

class MainActivity : AppCompatActivity() {
    private val sl = Shizuku.OnRequestPermissionResultListener { _,r -> if (r == PackageManager.PERMISSION_GRANTED) { Toast.makeText(this,"Shizuku OK",Toast.LENGTH_SHORT).show(); ShizukuBridge.grantOverlay(this) } }
    override fun onCreate(b: Bundle?) {
        super.onCreate(b); setContentView(R.layout.activity_main); Shizuku.addRequestPermissionResultListener(sl)
        findViewById<Button>(R.id.btn_grant_overlay).setOnClickListener { if (!Settings.canDrawOverlays(this)) startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")),1001) else Toast.makeText(this,"Overlay ja OK",Toast.LENGTH_SHORT).show() }
        findViewById<Button>(R.id.btn_check_shizuku).setOnClickListener { try { if (ShizukuBridge.isAvailable()) { if (ShizukuBridge.hasPermission()) { Toast.makeText(this,"Shizuku OK",Toast.LENGTH_SHORT).show(); ShizukuBridge.grantOverlay(this) } else ShizukuBridge.requestPermission(this) } else Toast.makeText(this,"Abra o Shizuku primeiro!",Toast.LENGTH_LONG).show() } catch (e: Exception) { Toast.makeText(this,"Shizuku nao instalado",Toast.LENGTH_LONG).show() } }
        findViewById<Button>(R.id.btn_start_mod).setOnClickListener { if (!Settings.canDrawOverlays(this)) { Toast.makeText(this,"Conceda overlay!",Toast.LENGTH_LONG).show(); return@setOnClickListener }; if (Build.VERSION.SDK_INT >= 33) requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),100); startForegroundService(Intent(this,FloatingOverlayService::class.java)); Toast.makeText(this,"Mod ativado! Abra o Free Fire",Toast.LENGTH_SHORT).show(); startActivity(Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_HOME); flags = Intent.FLAG_ACTIVITY_NEW_TASK }) }
        findViewById<Button>(R.id.btn_scripts).setOnClickListener { startActivity(Intent(this,ScriptsActivity::class.java)) }
    }
    override fun onActivityResult(r: Int, c: Int, d: Intent?) { super.onActivityResult(r,c,d); if (r == 1001 && Settings.canDrawOverlays(this)) Toast.makeText(this,"Overlay OK",Toast.LENGTH_SHORT).show() }
    override fun onDestroy() { Shizuku.removeRequestPermissionResultListener(sl); super.onDestroy() }
}
