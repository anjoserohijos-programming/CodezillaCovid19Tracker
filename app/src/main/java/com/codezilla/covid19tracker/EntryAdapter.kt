package com.codezilla.covid19tracker

import android.view.LayoutInflater
import android.view.ScrollCaptureCallback
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EntryAdapter : RecyclerView.Adapter<EntryAdapter.EntryViewHolder>(){
    private var stdList: ArrayList<CovidTrackerModel> = ArrayList()
    private var onClickItem: ((CovidTrackerModel) -> Unit)? =null
    private var onClickDeleteItem: ((CovidTrackerModel) -> Unit)? =null

    fun addItems(Items: ArrayList<CovidTrackerModel>){
        this.stdList = Items
        notifyDataSetChanged()
    }

    fun setonClickDeleteItem(callback: (CovidTrackerModel)->Unit){
        this.onClickDeleteItem = callback
    }
    fun setOnClickItem(callback: (CovidTrackerModel)->Unit){
        this.onClickItem = callback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= EntryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_items_cvd, parent, false))

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val std = stdList[position]
        holder.bindView(std)
        holder.itemView.setOnClickListener{ onClickItem?.invoke(std)}
        holder.btnDelete.setOnClickListener{onClickDeleteItem?.invoke(std)}
    }

    override fun getItemCount(): Int {
        return stdList.size
    }

    class EntryViewHolder(var view: View): RecyclerView.ViewHolder(view){
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var fullname = view.findViewById<TextView>(R.id.tvFullName)
        private var homeaddress = view.findViewById<TextView>(R.id.tvHomeAddress)
        var btnDelete = view.findViewById<Button>(R.id.btnDelete)

        fun bindView(std: CovidTrackerModel){
            id.text = std.id.toString()
            fullname.text = std.name
            homeaddress.text = std.homeAddress
        }
    }
}