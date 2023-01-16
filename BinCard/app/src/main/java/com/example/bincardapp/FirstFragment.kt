package com.example.bincardapp

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bincardapp.databinding.FragmentFirstBinding
import com.example.bincardapp.model.CardInfo
import com.google.gson.Gson
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@Suppress("DEPRECATION")
class FirstFragment : Fragment() {
    private lateinit var cardInfo2: CardInfo
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!  //45717360
    private val baseUrl = "https://lookup.binlist.net/"
    private var bankLink = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
    private var bankUriLink = Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
    lateinit var pDialog:ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val cardInfo = Gson().fromJson(loadJson(this.requireContext()), CardInfo::class.java)
        binding.cardViewInfo.visibility = View.INVISIBLE
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.submitBtn.setOnClickListener {
            var lastPartUrl = binding.edit.text.toString()
            var newUrl = baseUrl + lastPartUrl
            someTask(this).execute(newUrl)
        }
        binding.bankPhone.setOnClickListener{
            checkPermission()
        }
        binding.latitudeLongitude.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW,Uri.parse("geo:${cardInfo2.country.latitude},${cardInfo2.country.longitude}"))
            startActivity(intent)

        }
        binding.bankLink.setOnClickListener{
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), bankUriLink)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun loadJson(context: Context): String? {
        var input: InputStream? = null
        var jsonString: String
        try {
            // Create InputStream
            input = context.assets.open("json_data.json")
            val size = input.available()
            // Create a buffer with the size
            val buffer = ByteArray(size)
            // Read data from InputStream into the Buffer
            input.read(buffer)
            // Create a json String
            jsonString = String(buffer)
            return jsonString
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            // Must close the stream
            input?.close()
        }
        return null
    }
    @Suppress("DEPRECATION")
    inner class someTask(activity: FirstFragment) : AsyncTask<String, String, String>() {
        var mActivity:FirstFragment = activity
        override fun doInBackground(vararg url: String?): String? {
            var res: String = ""
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

        override fun onPreExecute() {
            super.onPreExecute()
            pDialog = ProgressDialog(this@FirstFragment.context)
            pDialog.setMessage("Please wait")
            pDialog.setCancelable(false)
            pDialog.show()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val cardInfo = Gson().fromJson(result, CardInfo::class.java)
            cardInfo2 = cardInfo
            cardInfo.bin = binding.edit.text.toString()
            updateUI(cardInfo)

        }

    }
    private fun updateUI(cardInfo:CardInfo) = with(binding){
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
            bankUriLink = Uri.parse(cardInfo.bank.url)
        if (pDialog.isShowing) pDialog.dismiss()
    }
    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    42)
            }
        } else {
            // Permission has already been granted
            callPhone()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 42) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay!
                callPhone()
            } else {
                // permission denied, boo! Disable the
                // functionality
            }
            return
        }
    }

    fun callPhone(){
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel: + ${binding.bankPhone.text}"))
        startActivity(intent)
    }
}

