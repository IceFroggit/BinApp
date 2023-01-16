package com.example.bincardapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.bincardapp.databinding.FragmentSelectedCardBinding
import com.example.bincardapp.model.CardInfo
import kotlinx.coroutines.internal.artificialFrame

class SelectedCardFragment : Fragment() {
    private var _binding: FragmentSelectedCardBinding? = null
    private val binding get() = _binding!!
    var bankUriLink = Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
    private lateinit var cardInfo:CardInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            val args = SelectedCardFragmentArgs.fromBundle(bundle!!)
            if (args.selectedCarditem != null)
                cardInfo = args.selectedCarditem
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectedCardBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            val args = SelectedCardFragmentArgs.fromBundle(bundle!!)
            if (args.selectedCarditem != null) {
                binding.apply {
                    cardViewInfo.visibility = View.VISIBLE
                    scheme.text = cardInfo?.scheme
                    brand.text = cardInfo?.brand
                    legnth.text = cardInfo?.number?.length.toString()
                    luhn.text = if (cardInfo?.number?.luhn == true) "Yes" else "No"
                    type.text = cardInfo?.type
                    val city = if (cardInfo?.bank?.city != null) cardInfo?.bank?.city else ""
                    if (cardInfo.prepaid != null)
                        prepaid.text = if (cardInfo?.prepaid == true) "Yes" else "No"
                    if(cardInfo?.bank?.name != null)
                        bankName.text = "${cardInfo?.bank?.name}, $city"
                    bankPhone.text = cardInfo?.bank?.phone ?: ""
                    bankLink.text = cardInfo?.bank?.url ?: ""
                    countryName.text = "${cardInfo?.country?.emoji}, ${cardInfo?.country?.name}"
                    latitudeLongitude.text ="(latitude: ${cardInfo.country.latitude},longitude: ${cardInfo.country.longitude})"
                    if (cardInfo?.bank?.url != null)
                        bankUriLink = Uri.parse(cardInfo?.bank?.url)
                }
            }
        }
        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_SelectedCardFragment_to_SecondFragment)
        }
    }

            private fun getData() {
                /*   var intent = intent.extras
        val bin = intent!!.getString("bin")
        binding.cardBin.text  = bin
     */
    }

}
