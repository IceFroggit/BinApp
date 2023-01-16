package com.example.bincardapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bincardapp.databinding.FragmentSelectedCardBinding
import com.example.bincardapp.model.CardInfo

class SelectedCardFragment : Fragment() {
    private var _binding: FragmentSelectedCardBinding? = null
    private val binding get() = _binding!!
    private lateinit var bankUriLink:Uri
    private lateinit var cardInfo:CardInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            val args = SelectedCardFragmentArgs.fromBundle(bundle)
            if (args.selectedCarditem != null)
                cardInfo = args.selectedCarditem
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedCardBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            val args = SelectedCardFragmentArgs.fromBundle(bundle)
            if (args.selectedCarditem != null)
                updateUI(cardInfo)
        }
        binding.latitudeLongitude.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW,Uri.parse("geo:${cardInfo.country.latitude},${cardInfo.country.longitude}"))
            startActivity(intent)
        }
        binding.bankPhone.setOnClickListener{
            checkPermission()
        }
        binding.bankLink.setOnClickListener{
            if (cardInfo.bank?.url != "")
                bankUriLink = Uri.parse("https://${cardInfo.bank!!.url}")
            val webIntent = Intent(Intent.ACTION_VIEW)
            webIntent.data = bankUriLink
            requireActivity().startActivity(webIntent)

        }
        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_SelectedCardFragment_to_SecondFragment)
        }
    }
    private fun updateUI(cardInfo:CardInfo) = with(binding){
        cardViewInfo.visibility = View.VISIBLE
        bin.text = cardInfo.bin
        scheme.text = cardInfo.scheme
        brand.text = cardInfo.brand
        if(cardInfo.number?.length != 0 ){
            legnth.text =  cardInfo.number?.length.toString()
            luhn.text = if (cardInfo.number?.luhn == true) "Yes" else "No"
        }
        else{
            legnth.text =""
            luhn.text = ""
        }
        type.text = cardInfo.type
        val city = cardInfo.bank?.city
        prepaid.text = if (cardInfo.prepaid == true) "Yes" else "No"
        bankName.text = "${cardInfo.bank?.name}, $city"
        bankPhone.text = cardInfo.bank?.phone
        bankLink.text = cardInfo.bank?.url
        countryName.text = "${cardInfo.country.emoji}, ${cardInfo.country.name}"
        latitudeLongitude.text ="(latitude: ${cardInfo.country.latitude},longitude: ${cardInfo.country.longitude})"
        if (cardInfo.bank?.url != ""){
            val urlTemp = cardInfo.bank?.url.toString()
            bankUriLink = Uri.parse("https://$urlTemp")
        }
    }
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    42)
            }
        } else {
            callPhone()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 42) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                callPhone()
            }
            return
        }
    }

    private fun callPhone(){
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel: + ${binding.bankPhone.text}"))
        startActivity(intent)
    }
}



