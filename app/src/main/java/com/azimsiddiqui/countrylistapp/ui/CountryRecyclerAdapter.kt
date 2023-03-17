package com.azimsiddiqui.countrylistapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azimsiddiqui.countrylistapp.databinding.RowItemLayoutBinding

class CountryRecyclerAdapter(private var listener: CountryItemClickListener) :
    RecyclerView.Adapter<CountryRecyclerAdapter.CountryViewHolder>() {

    private var countryList = ArrayList<String>()

    inner class CountryViewHolder(private val itemBinding: RowItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(name: String) {
            itemBinding.etName.text = name
            itemBinding.cardContainer.setOnClickListener {
                if (listener is MainActivity)
                    listener.onClick(name)
                else
                    listener.openBottomSheet()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val itemBinding =
            RowItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countryList[position])
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    fun setData(list: List<String>) {
        countryList.clear()
        countryList.addAll(list)
        notifyDataSetChanged()
    }

}

interface CountryItemClickListener {
    fun onClick(country: String)
    fun openBottomSheet()
}