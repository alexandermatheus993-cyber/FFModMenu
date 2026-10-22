function speedBoost()
    gg.setVisible(false)
    gg.toast("Procurando velocidade...")
    gg.searchNumber("1.0;1.0;1.0", gg.TYPE_FLOAT, false, gg.SIGN_EQUAL, 0, -1, 0)
    gg.refineNumber("1.0", gg.TYPE_FLOAT)
    local results = gg.getResults(500)
    if #results == 0 then gg.toast("Nenhum valor encontrado!") return end
    local mod = {}
    for i, v in ipairs(results) do mod[i] = { address = v.address, flags = gg.TYPE_FLOAT, value = 2.5 } end
    gg.setValues(mod)
    gg.toast("Speed 2.5x ativado!")
    gg.clearResults()
end
speedBoost()
