package com.example.bincardapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.bincardapp.R
import com.example.bincardapp.databinding.CardItemBinding
import com.example.bincardapp.model.CardInfo
import java.util.*
import kotlin.collections.ArrayList

class CardAdapter(private var clickListener: ClickListener):RecyclerView.Adapter<CardAdapter.CardViewHolder>(),Filterable{
    var cardList = ArrayList<CardInfo>()
    var cardListFiltered = ArrayList<CardInfo>()
    interface ClickListener{
        fun clickedItem(card: CardInfo)
    }

    class CardViewHolder(item:View):RecyclerView.ViewHolder(item){
        private val binding = CardItemBinding.bind(item)

        fun bind(card:CardInfo) = with(binding){
            bin.text = card.bin
            flagEmoji.text = card.country.emoji
            cardBankName.text = if (card.bank?.name != "no info") card.bank?.name else ""
            if(card.scheme == "visa")
                binding.cardImg.setBackgroundResource(R.drawable.visa)
            else
                binding.cardImg.setBackgroundResource(R.drawable.mastercard1)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item,parent,false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cardList[position]
        holder.bind(card)
        holder.itemView.setOnClickListener{
            clickListener.clickedItem(card)
        }
    }
    override fun getItemCount(): Int {
        return cardList.size
    }
    fun addCard(card: CardInfo){
        cardList.add(card)
        cardListFiltered.add(card)
        notifyDataSetChanged()
    }

    fun setData(dataList: ArrayList<CardInfo>){
        cardList = dataList
        cardListFiltered = cardList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        val filter = object: Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if(p0 == null || p0.isEmpty()){
                    filterResults.values = cardListFiltered
                    filterResults.count = cardListFiltered.size
                }
                else{
                    val searchChar = p0.toString().lowercase(Locale.getDefault())
                    val filteredResults = ArrayList<CardInfo>()

                    for (card in cardListFiltered){
                        if(card.bin.contains(searchChar))
                            filteredResults.add(card)
                    }
                    filterResults.values = filteredResults
                    filterResults.count = filteredResults.size
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                cardList = p1!!.values as ArrayList<CardInfo>
                notifyDataSetChanged()
            }
        }
        return filter
    }
}