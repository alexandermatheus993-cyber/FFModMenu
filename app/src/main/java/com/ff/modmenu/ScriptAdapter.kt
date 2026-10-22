package com.ff.modmenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScriptAdapter(val items: List<ModScript>, val onRun: (ModScript)->Unit, val onToggle: (ModScript,Boolean)->Unit, val onDel: (ModScript)->Unit) : RecyclerView.Adapter<ScriptAdapter.VH>() {
    class VH(v: View) : RecyclerView.ViewHolder(v) { val n: TextView = v.findViewById(R.id.tv_script_name); val p: TextView = v.findViewById(R.id.tv_script_path); val sw: Switch = v.findViewById(R.id.switch_script); val r: Button = v.findViewById(R.id.btn_run_script); val d: Button = v.findViewById(R.id.btn_delete_script) }
    override fun onCreateViewHolder(p: ViewGroup, t: Int) = VH(LayoutInflater.from(p.context).inflate(R.layout.item_script, p, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(h: VH, i: Int) { val s = items[i]; h.n.text = s.name; h.p.text = if (s.isBuiltIn) "built-in" else s.filePath; h.sw.isChecked = s.isEnabled; h.d.visibility = if (s.isBuiltIn) View.GONE else View.VISIBLE; h.sw.setOnCheckedChangeListener { _,c -> onToggle(s,c) }; h.r.setOnClickListener { onRun(s) }; h.d.setOnClickListener { onDel(s) } }
}
