package com.example.isslocationtrackerapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.isslocationtrackerapp.R
import com.example.isslocationtrackerapp.data.model.IssLocationData
import com.example.isslocationtrackerapp.databinding.IssLocationListItemBinding
import com.example.isslocationtrackerapp.util.FormatUtils

class IssLocationAdapter : RecyclerView.Adapter<IssLocationAdapter.IssLocationViewHolder>() {

    private val issLocationDataLists = mutableListOf<IssLocationData>()

    fun setList(issLocations: List<IssLocationData>){
        issLocationDataLists.clear()
        issLocationDataLists.addAll(issLocations)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssLocationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : IssLocationListItemBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.iss_location_list_item,
            parent,
            false
        )
        return IssLocationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return issLocationDataLists.size
    }

    override fun onBindViewHolder(holder: IssLocationViewHolder, position: Int) {
        holder.bind(issLocationDataLists[position])
    }

    class IssLocationViewHolder(val binding: IssLocationListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(issLocationData: IssLocationData){
            val lat = FormatUtils.getFormatted(issLocationData.issPosition.latitude.toDouble())
            val long = FormatUtils.getFormatted(issLocationData.issPosition.longitude.toDouble())
            val timeEst = FormatUtils.formatToDateString(issLocationData.timestamp)
            val latLong = "Lat: $lat Long: $long / Time: $timeEst"
            binding.issLocationTextView.text = latLong
        }
    }
}
