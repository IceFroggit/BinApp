package com.example.bincardapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bincardapp.R
import com.example.bincardapp.databinding.CardItemBinding
import com.example.bincardapp.model.CardInfo

class CardAdapter(clickListener: ClickListener):RecyclerView.Adapter<CardAdapter.CardViewHolder>(){
    val cardList = ArrayList<CardInfo>()
    private var clickListener:ClickListener = clickListener
    interface ClickListener{
        fun clickedItem(card: CardInfo)
    }

    class CardViewHolder(item:View):RecyclerView.ViewHolder(item){
        val binding = CardItemBinding.bind(item)

        fun bind(card:CardInfo) = with(binding){
            if (card.scheme == "mastercard")
                cardImg.setBackgroundResource(R.drawable.mastercard1)
            bin.text = card.bin
            flagEmoji.text = card.country.emoji
            cardBankName.text = card.bank?.name

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
        notifyDataSetChanged()
    }
}