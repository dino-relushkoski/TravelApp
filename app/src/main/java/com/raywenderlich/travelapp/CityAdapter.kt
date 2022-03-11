package com.raywenderlich.travelapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CityAdapter : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    private var ctyList: ArrayList<CityModel> = ArrayList()
    private var onClickItem: ((CityModel) -> Unit)? = null
    private var onClickDeleteItem: ((CityModel) -> Unit)? = null


    fun addItems(items: ArrayList<CityModel>) {
        this.ctyList = items
        notifyDataSetChanged()

    }

    fun setOnClickItem(callback: (CityModel) -> Unit){
        this.onClickItem=callback
    }

    fun setOnClickDeleteItem(callback: (CityModel) -> Unit){
        this.onClickDeleteItem=callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CityViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_cty, parent, false)
    )

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val cty = ctyList[position]
        holder.bindView(cty)
        holder.itemView.setOnClickListener{onClickItem?.invoke(cty)}
        holder.btnDelete.setOnClickListener{onClickDeleteItem?.invoke(cty)}
    }

    override fun getItemCount(): Int {
        return ctyList.size
    }


    class CityViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var city = view.findViewById<TextView>(R.id.tvCity)
        private var country = view.findViewById<TextView>(R.id.tvCountry)
        private var description = view.findViewById<TextView>(R.id.tvDescription)
        var btnDelete = view.findViewById<Button>(R.id.btnDelete)

        fun bindView(cty: CityModel) {
            id.text = cty.id.toString()
            city.text = cty.city
            country.text = cty.country
            description.text = cty.description

        }

    }


}