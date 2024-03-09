package com.example.btmonitork

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.btmonitork.databinding.HistoryItemBinding


class HistoryAdapter(private val listener: HistoryActivity) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>()
{// ListAdapter<ListItem, RcAdapter.ItemHolder>(ItemComparator())
    val historyList= ArrayList<HistoryItem>()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding=HistoryItemBinding.bind(itemView)

        fun bind(historyItem:HistoryItem, listener: Listener)= with(binding){
            idText.text=historyItem.id+"."
            if(historyItem.prediction.equals("0")){
                predictionText.setTextColor(Color.parseColor("#00C322"))
            }
            else if(historyItem.prediction.equals("1")){
                predictionText.setTextColor(Color.parseColor("#FF1300"))
            }
            predictionText.text=historyItem.prediction
            dateText.text=historyItem.date

            itemView.setOnClickListener{
                listener.OnClick(historyItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(historyList[position],listener)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
    fun addHistory(histort:HistoryItem){
        historyList.add(histort)
        notifyDataSetChanged()
    }
    fun Clear(){
        historyList.clear()
        notifyDataSetChanged()
    }
    interface Listener{
        fun OnClick(item: HistoryItem)
    }
}