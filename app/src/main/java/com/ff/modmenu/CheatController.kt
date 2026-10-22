package com.ff.modmenu

object CheatController {
    enum class Mode { GAMEGUARDIAN, SHIZUKU }
    var mode = Mode.GAMEGUARDIAN
    private val cheats = mutableMapOf("high_damage" to false, "zero_recoil" to false, "speed_hack" to false, "aim_assist" to false, "god_mode" to false)
    fun toggle(id: String): Boolean { val n = !(cheats[id] ?: false); cheats[id] = n; return n }
    fun isActive(id: String) = cheats[id] ?: false
    fun deactivateAll() { cheats.keys.forEach { cheats[it] = false } }
}
