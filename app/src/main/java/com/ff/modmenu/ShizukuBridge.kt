package com.ff.modmenu

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import rikka.shizuku.Shizuku

object ShizukuBridge {
    fun isAvailable() = try { Shizuku.pingBinder() } catch (_: Exception) { false }
    fun hasPermission() = try { Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED } catch (_: Exception) { false }
    fun requestPermission(ctx: Context) { try { if (isAvailable() && !hasPermission()) Shizuku.requestPermission(0) } catch (e: Exception) { Toast.makeText(ctx, "Erro: ${e.message}", Toast.LENGTH_SHORT).show() } }
    fun run(cmd: String) = try { val p = Shizuku.newProcess(arrayOf("sh","-c",cmd),null,null); val o = p.inputStream.bufferedReader().readText(); p.waitFor(); o.ifEmpty{"OK"} } catch (e: Exception) { "ERR: ${e.message}" }
    fun grantOverlay(ctx: Context) = run("appops set ${ctx.packageName} SYSTEM_ALERT_WINDOW allow")
}
