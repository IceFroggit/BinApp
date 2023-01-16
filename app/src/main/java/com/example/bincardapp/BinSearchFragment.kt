@file:Suppress("DEPRECATION")

package com.example.bincardapp

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bincardapp.dao.CardInfoViewModel
import com.example.bincardapp.databinding.FragmentFirstBinding
import com.example.bincardapp.model.Bank
import com.example.bincardapp.model.Number
import com.example.bincardapp.model.CardInfo
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL


@Suppress("DEPRECATION")
class BinSearchFragment : Fragment() {
    private lateinit var mCardInfoViewModel: CardInfoViewModel
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val baseUrl = "https://lookup.binlist.net/"
    private lateinit var bankUriLink:Uri
    lateinit var pDialog:ProgressDialog
    lateinit var cardInfo: CardInfo
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        mCardInfoViewModel = ViewModelProvider(this).get(CardInfoViewModel::class.java)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        binding.cardViewInfo.visibility = View.INVISIBLE
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.submitBtn.setOnClickListener {
            if(!(binding.edit.text.isEmpty() || binding.edit.text.length < 8 || binding.edit.text.length > 8 )){
                val lastPartUrl = binding.edit.text.toString()
                val newUrl = baseUrl + lastPartUrl
                DownloadJson().execute(newUrl)
            }
            else{
                Toast.makeText(context,"Enter BIN containing 8 numbers",Toast.LENGTH_SHORT).show()
            }
        }
        binding.bankPhone.setOnClickListener{
            checkPermission()
        }
        binding.latitudeLongitude.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW,Uri.parse("geo:${cardInfo.country.latitude},${cardInfo.country.longitude}"))
            startActivity(intent)

        }
        binding.bankLink.setOnClickListener{
            val webIntent = Intent(Intent.ACTION_VIEW)
            webIntent.data = bankUriLink
            requireActivity().startActivity(webIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Suppress("DEPRECATION")
    inner class DownloadJson : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            pDialog = ProgressDialog(this@BinSearchFragment.context)
            pDialog.setMessage("Please wait")
            pDialog.setCancelable(false)
            pDialog.show()
        }

        override fun doInBackground(vararg url: String?): String? {
            if(isConnectedToServer(url[0],1000)){
                var res = ""
                val connection = URL(url[0]).openConnection() as HttpURLConnection
                try {
                    connection.connect()
                    res = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
                } catch (e: Exception) {
                } finally {
                    connection.disconnect()
                }
                return res
            }
           return null
        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(result == null || result.isEmpty()){
                pDialog.dismiss()
                Toast.makeText(context,"Card with this bin doesn't exist",Toast.LENGTH_SHORT).show()
            }
            else{
                cardInfo = checkData(Gson().fromJson(result, CardInfo::class.java))
                cardInfo.bin = binding.edit.text.toString()
                updateUI(cardInfo)
                insertDataToDatabase(cardInfo)
            }


        }

    }
    private fun updateUI(cardInfo:CardInfo) = with(binding){
        cardViewInfo.visibility = View.VISIBLE
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
        if (pDialog.isShowing) pDialog.dismiss()
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

    @Deprecated("Deprecated in Java")
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
    fun isConnectedToServer(url: String?, timeout: Int): Boolean {
        return try {
            val myUrl = URL(url)
            val connection = myUrl.openConnection() as HttpURLConnection
            connection.connectTimeout = timeout
            connection.connect()
            true
        } catch (e: java.lang.Exception) {
            false
        }
    }
    private fun insertDataToDatabase(card: CardInfo){
        mCardInfoViewModel.addCard(card)
        Toast.makeText(requireContext(),"Successfully added info to database!",Toast.LENGTH_LONG).show()
    }

    fun checkData(card: CardInfo): CardInfo {
        card.bank = card.bank ?: Bank("", "no info", "", "")
        card.bank?.name = card.bank?.name ?: "no info"
        card.bank?.city = card.bank?.city ?: ""
        card.bank?.url = card.bank?.url ?: ""
        card.bank?.phone = card.bank?.phone ?: ""
        card.number?.luhn = card.number?.luhn ?: false
        card.brand = card.brand ?: "no info"
        card.number = card.number ?: Number(0, false)
        card.prepaid = card.prepaid ?: false
        card.type = card.type ?: "none"
        return card
    }
}

