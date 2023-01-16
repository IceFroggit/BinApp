package com.example.bincardapp

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bincardapp.adapter.CardAdapter
import com.example.bincardapp.dao.CardInfoViewModel
import com.example.bincardapp.databinding.FragmentSecondBinding
import com.example.bincardapp.model.CardInfo


@Suppress("DEPRECATION")
class BinListFragment : Fragment(), CardAdapter.ClickListener {

    private lateinit var mCardInfoViewModel: CardInfoViewModel
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView:RecyclerView
    private val adapter:CardAdapter = CardAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        mCardInfoViewModel = ViewModelProvider(this).get(CardInfoViewModel::class.java)
        mCardInfoViewModel.readAllData.observe(viewLifecycleOwner, Observer { cardList ->
            adapter.setData(cardList as ArrayList<CardInfo>)
        })
        initRecyclerView()
        //тулбар
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbarList)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn2.setOnClickListener{
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    private fun initRecyclerView(){
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
        val directions = BinListFragmentDirections.actionSecondFragmentToSelectedCardFragment(card)
        Navigation.findNavController(requireView()).navigate(directions)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.search_bar,menu)
        val menuItem = menu.findItem(R.id.search_view)
        val searchView = menuItem!!.actionView as SearchView
        searchView.queryHint = "Search by BIN"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu, inflater)
    }

}