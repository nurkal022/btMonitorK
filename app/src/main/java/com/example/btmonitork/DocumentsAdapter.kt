package com.example.btmonitork

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DocumentsAdapter(private val documents: List<Document>, private val listener: OnItemLongClickListener) : RecyclerView.Adapter<DocumentsAdapter.DocumentViewHolder>() {

    class DocumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idtext:TextView=itemView.findViewById(R.id.idText)
        val nametext:TextView=itemView.findViewById(R.id.nameText)
        val datatext:TextView=itemView.findViewById(R.id.dataText)
        init {
            itemView.setOnLongClickListener {
                // Действия по удалению элемента
                // Возвращаем true, чтобы указать, что событие долгого нажатия было обработано
                true
            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.document_item, parent, false)
        return DocumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val ItemViewModel=documents[position]
        holder.idtext.text=ItemViewModel.id
        holder.nametext.text=ItemViewModel.name

        holder.datatext.text=ItemViewModel.data

        // Установка данных для элементов в holder

        holder.itemView.setOnLongClickListener {
            listener.onItemLongClicked(position)
            true
        }


    }




    override fun getItemCount() = documents.size


    interface OnItemLongClickListener {
        fun onItemLongClicked(position: Int)
    }
}


