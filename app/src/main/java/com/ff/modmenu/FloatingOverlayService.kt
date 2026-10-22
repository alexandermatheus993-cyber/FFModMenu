package com.ff.modmenu

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.widget.*
import androidx.core.app.NotificationCompat

class FloatingOverlayService : Service() {
    private lateinit var wm: WindowManager
    private lateinit var fab: View
    private lateinit var panel: View
    private var panelOn = false
    private val btns = mutableMapOf<String, Button>()
    override fun onBind(i: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        startForeground(1, NotificationCompat.Builder(this, App.CHANNEL_ID).setContentTitle("FF Mod Menu").setContentText("Toque MOD").setSmallIcon(android.R.drawable.ic_menu_manage).setOngoing(true).setContentIntent(PendingIntent.getActivity(this,0,Intent(this,MainActivity::class.java),PendingIntent.FLAG_IMMUTABLE)).build())
        createFab(); createPanel()
    }

    private fun createFab() {
        fab = TextView(this).apply { text = "MOD"; textSize = 14f; setTextColor(Color.WHITE); setBackgroundColor(0xFF00BCD4.toInt()); setPadding(28,20,28,20) }
        val p = WindowManager.LayoutParams(WRAP_CONTENT,WRAP_CONTENT,WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,PixelFormat.TRANSLUCENT).apply { gravity = Gravity.TOP or Gravity.START; x = 16; y = 300 }
        var ix = 0; var iy = 0; var tx = 0f; var ty = 0f; var drag = false
        fab.setOnTouchListener { _,e -> when (e.action) { MotionEvent.ACTION_DOWN -> { ix=p.x; iy=p.y; tx=e.rawX; ty=e.rawY; drag=false; true }; MotionEvent.ACTION_MOVE -> { p.x=ix+(e.rawX-tx).toInt(); p.y=iy+(e.rawY-ty).toInt(); wm.updateViewLayout(fab,p); drag=true; true }; MotionEvent.ACTION_UP -> { if (!drag) toggle(); true }; else -> false } }
        wm.addView(fab, p)
    }

    private fun createPanel() {
        panel = LayoutInflater.from(android.view.ContextThemeWrapper(this, R.style.Theme_FFModMenu)).inflate(R.layout.overlay_panel, null)
        listOf(R.id.btn_damage to "high_damage", R.id.btn_recoil to "zero_recoil", R.id.btn_speed to "speed_hack", R.id.btn_aim to "aim_assist", R.id.btn_godmode to "god_mode").forEach { (id,cid) -> val b = panel.findViewById<Button>(id); btns[cid] = b; paintBtn(b,cid); b.setOnClickListener { toggleCheat(cid) } }
        val mt = panel.findViewById<Button>(R.id.btn_mode_toggle); paintMode(mt)
        mt.setOnClickListener { CheatController.mode = if (CheatController.mode == CheatController.Mode.GAMEGUARDIAN) CheatController.Mode.SHIZUKU else CheatController.Mode.GAMEGUARDIAN; paintMode(mt); Toast.makeText(this,"Modo: ${CheatController.mode}",Toast.LENGTH_SHORT).show() }
        panel.findViewById<Button>(R.id.btn_scripts).setOnClickListener { startActivity(Intent(this,ScriptsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
        panel.findViewById<Button>(R.id.btn_close).setOnClickListener { toggle() }
        panel.findViewById<Button>(R.id.btn_exit).setOnClickListener { CheatController.deactivateAll(); stopSelf() }
        val pp = WindowManager.LayoutParams((300*resources.displayMetrics.density).toInt(),WRAP_CONTENT,WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,PixelFormat.TRANSLUCENT).apply { gravity = Gravity.CENTER }
        wm.addView(panel,pp); panel.visibility = View.GONE
    }

    private fun toggle() { panelOn = !panelOn; panel.visibility = if (panelOn) View.VISIBLE else View.GONE }

    private fun toggleCheat(id: String) {
        val on = CheatController.toggle(id); btns[id]?.let { paintBtn(it,id) }
        when (CheatController.mode) { CheatController.Mode.GAMEGUARDIAN -> GameGuardianBridge.executeCheat(this,id,on); CheatController.Mode.SHIZUKU -> ShizukuBridge.run("echo $id ${if (on) "ON" else "OFF"}") }
        Toast.makeText(this,"${id.replace("_"," ").uppercase()}: ${if (on) "ON" else "OFF"}",Toast.LENGTH_SHORT).show()
    }

    private fun paintBtn(b: Button, id: String) { val on = CheatController.isActive(id); b.setBackgroundColor(if (on) 0xFFFF5722.toInt() else 0xFF4CAF50.toInt()); b.text = "${id.replace("_"," ").uppercase()}  ${if (on) "ON" else "OFF"}"; b.setTextColor(Color.WHITE) }
    private fun paintMode(b: Button) { b.text = "MODO: ${CheatController.mode}"; b.setBackgroundColor(if (CheatController.mode == CheatController.Mode.GAMEGUARDIAN) 0xFF4CAF50.toInt() else 0xFF2196F3.toInt()); b.setTextColor(Color.WHITE) }
    private val WRAP_CONTENT = WindowManager.LayoutParams.WRAP_CONTENT

    override fun onDestroy() { try { wm.removeView(fab) } catch (_: Exception) {}; try { wm.removeView(panel) } catch (_: Exception) {}; super.onDestroy() }
}
