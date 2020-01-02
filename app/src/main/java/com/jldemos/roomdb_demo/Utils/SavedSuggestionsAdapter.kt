package com.jldemos.roomdb_demo.Utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jldemos.roomdb_demo.R
import kotlinx.android.synthetic.main.item_suggestion.view.*

class SavedSuggestionsAdapter: RecyclerView.Adapter<SavedSuggestionsAdapter.SuggestionViewHolder>() {

    var adapterItems: List<SuggestionModel> = emptyList()

    fun updateAdapter(items: List<SuggestionModel>){
        adapterItems = items
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SuggestionViewHolder(layoutInflater.inflate(R.layout.item_suggestion, parent, false))
    }

    override fun getItemCount() = adapterItems.size

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        holder.bind(adapterItems[position])
    }

    inner class SuggestionViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(item: SuggestionModel){
            itemView.apply {
                entryId.text = item.dbKey.toString()
                entrySuggestion.text = item.suggestion
            }
        }
    }
}