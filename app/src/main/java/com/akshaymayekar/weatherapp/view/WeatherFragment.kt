package com.akshaymayekar.weatherapp.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.akshaymayekar.weatherapp.R
import com.akshaymayekar.weatherapp.databinding.FragmentFirstBinding
import com.akshaymayekar.weatherapp.domain.model.WeatherInfo
import com.akshaymayekar.weatherapp.util.Response
import com.akshaymayekar.weatherapp.util.Utils
import com.akshaymayekar.weatherapp.view.adapter.City
import com.akshaymayekar.weatherapp.viewmodel.WeatherViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var weatherViewModel: WeatherViewModel

    var listView: ListView? = null
    var list =  Utils.getCityList()
    var map =  Utils.getCityCoordinates()
    lateinit var arrayAdapter: ArrayAdapter<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherViewModel.getWeatherDataFromLocal()
        setSearchAdapter()
        observeDataUpdate()
    }

    private fun setSearchAdapter() {


        arrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1, list
        )


        _binding?.listview?.adapter = arrayAdapter
        _binding?.listview?.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ -> updateSelection(position) }

        _binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (list.contains(query)) {
                    arrayAdapter.filter.filter(query)
                } else {
                    Toast.makeText(requireContext(), "No Match found", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText.isNullOrBlank()){
                    binding.listview.visibility = View.GONE
                }else {
                    binding.listview.visibility = View.VISIBLE
                    arrayAdapter.filter.filter(newText);
                }
                return false
            }

        })

    }

    private fun updateSelection(position: Int) {
        _binding.let {
            _binding?.searchView?.setQuery(arrayAdapter.getItem(position).toString(), false)
            _binding?.listview?.visibility = View.GONE

        }
        val value = map.getValue(arrayAdapter.getItem(position).toString())

       hideKeyboard()
        showLoadingDialog()
        weatherViewModel.getWeatherData(value.lat,value.lon)
    }


    private fun Context.hideKeyboard(view: View) {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }
    private fun showLoadingDialog() {
        _binding?.let {
            it.progressCircular.visibility = View.VISIBLE
        }
    }
    private fun hideLoadingDialog() {
        _binding?.let {
            it.progressCircular.visibility = View.GONE
        }
    }

    private fun observeDataUpdate() {

        CoroutineScope(Dispatchers.Main).launch {
            weatherViewModel.weatherState.collect { response ->

                when (response) {
                    is Response.Failure -> {
                        hideLoadingDialog()
                        Toast.makeText(
                        requireContext(),
                        "Something went Wrong...",
                        Toast.LENGTH_SHORT
                    ).show()}
                    is Response.Loading -> showLoadingDialog()
                    is Response.Success -> setDataToUI(response.data)
                }

            }
        }
    }

    private fun setDataToUI(data: WeatherInfo?) {

        hideLoadingDialog()
        data?.let {

            val jsonString = Gson().toJson(data)
            weatherViewModel.saveToLocalDB(jsonString)
            _binding?.let {
                it.tvTemp.text = (data.main.temp - 273.15).toInt().toString() + "\u2103"
                it.tvName.text = data.name
                it.tvMain.text = data.weather[0].main
                it.tvTempMin.text =
                    "L - " + (data.main.temp_min - 273.15).toInt().toString() + "\u2103"
                it.tvTempMax.text =
                    "H - " + (data.main.temp_max - 273.15).toInt().toString() + "\u2103"

                if(data.weather[0].icon.equals( "01d",ignoreCase = true)){
                    it.ivIcon.setImageResource(R.mipmap.ic_few_clear_sky)
                }else if(data.weather[0].icon.equals( "01n",ignoreCase = true)){
                    it.ivIcon.setImageResource(R.mipmap.ic_clearsky_night)
                }else if(data.weather[0].icon.equals( "02d",ignoreCase = true)){
                    it.ivIcon.setImageResource(R.mipmap.ic_few_clouds)
                }else if(data.weather[0].icon.equals( "02n",ignoreCase = true)){
                    it.ivIcon.setImageResource(R.mipmap.ic_few_clouds_night)
                }else if(data.weather[0].icon.equals( "03d",ignoreCase = true) or data.weather[0].icon.equals( "03dn",ignoreCase = true)){
                    it.ivIcon.setImageResource(R.mipmap.ic_few_scattered_clouds)
                }else if(data.weather[0].icon.equals( "04d",ignoreCase = true) or data.weather[0].icon.equals( "04n",ignoreCase = true)){
                    it.ivIcon.setImageResource(R.mipmap.ic_broken_clouds)
                }else if(data.weather[0].icon.equals( "09d",ignoreCase = true) or data.weather[0].icon.equals( "09n",ignoreCase = true)){
                    it.ivIcon.setImageResource(R.mipmap.ic_few_shower_rain)
                }else if(data.weather[0].icon.equals( "10d",ignoreCase = true)){
                    it.ivIcon.setImageResource(R.mipmap.ic_rain)
                }else if(data.weather[0].icon.equals( "10n",ignoreCase = true)){
                    it.ivIcon.setImageResource(R.mipmap.ic_rain_night)
                }else if(data.weather[0].icon.equals( "11d",ignoreCase = true)or data.weather[0].icon.equals( "11n",ignoreCase = true)){
                    it.ivIcon.setImageResource(R.mipmap.ic_thunderstorm)
                }else if(data.weather[0].icon.equals( "13d",ignoreCase = true) or data.weather[0].icon.equals( "13n",ignoreCase = true)){
                    it.ivIcon.setImageResource(R.mipmap.ic_snow)
                }else if(data.weather[0].icon.equals( "50d",ignoreCase = true) or data.weather[0].icon.equals( "50n",ignoreCase = true)){
                    it.ivIcon.setImageResource(R.mipmap.ic_mist)
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}