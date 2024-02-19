package cz.olgapec.myway

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WalkAdapter(private val walksList: List<Walk>) : RecyclerView.Adapter<WalkAdapter.WalkViewHolder>() {

    class WalkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Inicializace view položek, například TextView pro zobrazení informací o procházce
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.walk_item_layout, parent, false)
        return WalkViewHolder(view)
    }

    override fun onBindViewHolder(holder: WalkViewHolder, position: Int) {
        val walk = walksList[position]
        // Nastavení hodnot do view položek v holderu
        holder.itemView.findViewById<TextView>(R.id.textViewId).text = "ID: ${walk.id}"
        holder.itemView.findViewById<TextView>(R.id.textViewSteps).text = "Steps: ${walk.stepCount}"

    }

    override fun getItemCount(): Int {
        return walksList.size
    }
}