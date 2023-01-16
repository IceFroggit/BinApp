package com.example.bincardapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.example.bincardapp.databinding.ActivitySelectedCardBinding

class SelectedCardActivity : AppCompatActivity() {
    private var _binding: ActivitySelectedCardBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySelectedCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
        binding.backBtn.setOnClickListener {
            //findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

        }
    }

    private fun initData() = with(binding){}

    private fun getData(){
        var intent = intent.extras
        val bin = intent!!.getString("bin")
        binding.cardBin.text  = bin

    }

}