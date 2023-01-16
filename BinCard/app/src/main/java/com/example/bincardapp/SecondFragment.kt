package com.example.bincardapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bincardapp.adapter.CardAdapter
import com.example.bincardapp.databinding.FragmentSecondBinding
import com.example.bincardapp.model.Bank
import com.example.bincardapp.model.CardInfo
import com.example.bincardapp.model.Country
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(), CardAdapter.ClickListener {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    lateinit var recyclerView:RecyclerView
    private val adapter:CardAdapter = CardAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        initRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var bank = Bank("Aktau","Jusan","89992431186","https://binlist.net/")
        var country = Country("123","DKK","\uD83C\uDDE9\uD83C\uDDF0",56,73,"Denmark","128")
        var number = com.example.bincardapp.model.Number(16,true)
        var card = CardInfo("4276 5500",bank,"Debit",country,number,true,"visa","Debit")
        var card1 = CardInfo("5432 4521",bank,"Credit",country,number,true,"mastercard","Debit")
        adapter.addCard(card)
        adapter.addCard(card1)
        adapter.addCard(card)
        adapter.addCard(card1)
        for (i in 0 until 50){
            adapter.addCard(card)
        }


        /*binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }*/
    }
    fun initRecyclerView(){
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun clickedItem(card: CardInfo) {
       // startActivity(Intent(requireContext(), SelectedCardActivity::class.java).putExtra("bin",card.bin))
        //findNavController().navigate(R.id.action_SecondFragment_to_SelectedCardFragment)
        val directions = SecondFragmentDirections.actionSecondFragmentToSelectedCardFragment(card)
        Navigation.findNavController(view!!).navigate(directions)
    }



}