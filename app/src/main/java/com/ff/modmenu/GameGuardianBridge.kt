package com.ff.modmenu

import android.content.Context
import android.content.Intent
import android.widget.Toast

object GameGuardianBridge {
    private const val PKG = "com.njzm.gameguardian"
    private val SCRIPTS = mapOf(
        "high_damage" to """gg.setVisible(false) gg.searchNumber("100;200;300",gg.TYPE_DWORD) gg.refineNumber("200",gg.TYPE_DWORD) local r=gg.getResults(200) for i,v in ipairs(r) do v.value=99999 end gg.setValues(r) gg.toast("Dano Alto ON") gg.clearResults()""",
        "zero_recoil" to """gg.setVisible(false) gg.searchNumber("0.01;0.02;0.03",gg.TYPE_FLOAT) gg.refineNumber("0.02",gg.TYPE_FLOAT) local r=gg.getResults(200) for i,v in ipairs(r) do v.value=0.0 v.freeze=true end gg.setValues(r) gg.toast("Recoil Zero ON") gg.clearResults()""",
        "speed_hack" to """gg.setVisible(false) gg.searchNumber("1.0;0.5;0.25",gg.TYPE_FLOAT) gg.refineNumber("1.0",gg.TYPE_FLOAT) local r=gg.getResults(300) for i,v in ipairs(r) do v.value=2.5 v.freeze=true end gg.setValues(r) gg.toast("Speed 2.5x ON") gg.clearResults()""",
        "aim_assist" to """gg.setVisible(false) gg.searchNumber("0.3;0.5;0.8",gg.TYPE_FLOAT) gg.refineNumber("0.5",gg.TYPE_FLOAT) local r=gg.getResults(200) for i,v in ipairs(r) do v.value=1.0 v.freeze=true end gg.setValues(r) gg.toast("Aim Assist ON") gg.clearResults()""",
        "god_mode" to """gg.setVisible(false) gg.searchNumber("200",gg.TYPE_DWORD) gg.refineNumber("200",gg.TYPE_DWORD) local r=gg.getResults(100) for i,v in ipairs(r) do v.value=99999 v.freeze=true end gg.setValues(r) gg.toast("God Mode ON") gg.clearResults()"""
    )
    fun executeCheat(ctx: Context, id: String, on: Boolean) {
        val s = if (on) SCRIPTS[id] ?: return else "gg.removeList() gg.toast(\"$id OFF\")"
        send(ctx, s)
    }
    fun executeScript(ctx: Context, lua: String) = send(ctx, lua)
    private fun send(ctx: Context, s: String) {
        try {
            ScriptManager.saveScript(ctx, "_exec", s)
            val i = ctx.packageManager.getLaunchIntentForPackage(PKG)
            if (i != null) { i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); ctx.startActivity(i); Toast.makeText(ctx, "Enviado ao GG", Toast.LENGTH_SHORT).show() }
            else Toast.makeText(ctx, "GG nao encontrado!", Toast.LENGTH_LONG).show()
        } catch (e: Exception) { Toast.makeText(ctx, "Erro: ${e.message}", Toast.LENGTH_SHORT).show() }
    }
}
