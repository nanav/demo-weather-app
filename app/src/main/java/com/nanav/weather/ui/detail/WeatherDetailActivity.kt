package com.nanav.weather.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.nanav.weather.R
import com.nanav.weather.arch.BaseMvvmActivity
import com.nanav.weather.data.model.Weather
import com.nanav.weather.data.model.WeatherItem
import com.nanav.weather.databinding.ActivityWeatherDetailBinding
import com.nanav.weather.databinding.ElementWeatherBinding
import com.nanav.weather.ext.android.extra
import com.nanav.weather.ext.android.hideKeyboard
import com.nanav.weather.ext.rx.toCelsius
import com.nanav.weather.ui.SimpleAdapter

class WeatherDetailActivity :
    BaseMvvmActivity<WeatherDetailViewModel, ActivityWeatherDetailBinding>(
        WeatherDetailViewModel::class,
        ActivityWeatherDetailBinding::inflate
    ) {

    private val searchTerm by extra<String>(ARG_SEARCH)

    private val weatherAdapter by lazy {
        SimpleAdapter(
            ElementWeatherBinding::inflate,
            this::bindAdapterView
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.weatherDetailDataState.observe { processDataState(it) }

        layout.weatherItemList.adapter = weatherAdapter
        layout.weatherItemList.layoutManager = LinearLayoutManager(this)
        layout.weatherItemList.itemAnimator = DefaultItemAnimator()

        viewModel.search(searchTerm)
    }

    private fun processDataState(weatherDataState: WeatherDataState) {
        when (weatherDataState) {
            WeatherDataState.WeatherDataLoading -> showProgress(true)
            is WeatherDataState.WeatherDataError -> showErrorMessage(weatherDataState.errorMessage)
            is WeatherDataState.WeatherData -> setData(weatherDataState.weather)
        }
    }

    private fun setData(weather: Weather) {
        hideKeyboard()

        showProgress(false)

        layout.weatherCity.text = weather.city
        layout.weatherHumidity.text = String.format(
            getString(R.string.weather_detail_hum_value),
            weather.weatherMain.humidity.toInt()
        )
        layout.weatherTemp.text = String.format(
            getString(R.string.weather_detail_temp_value),
            weather.weatherMain.temp.toCelsius(),
            weather.weatherMain.feelsLike.toCelsius()
        )

        weatherAdapter.addItems(weather.weatherItems)
    }

    private fun bindAdapterView(
        holder: SimpleAdapter<WeatherItem, ElementWeatherBinding>.SimpleViewHolder,
        weatherItem: WeatherItem
    ) {
        holder.layout.weatherMain.text =
            String.format(getString(R.string.weather_detail_field_title, weatherItem.main))
        holder.layout.weatherDesc.text = weatherItem.description
    }


    private fun showProgress(isLoading: Boolean) {
        layout.weatherProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        layout.weatherInfo.visibility = if (isLoading) View.GONE else View.VISIBLE
        layout.weatherError.visibility = View.GONE
    }

    private fun showErrorMessage(@StringRes message: Int) {
        layout.weatherProgress.visibility = View.GONE
        layout.weatherInfo.visibility = View.GONE
        layout.weatherError.visibility = View.VISIBLE
        layout.weatherError.text = getString(message)
    }

    companion object {

        const val ARG_SEARCH = "search.String"

        fun start(activity: Activity, searchTerm: String) {
            val intent = Intent(activity, WeatherDetailActivity::class.java)
            intent.putExtra(ARG_SEARCH, searchTerm)
            activity.startActivity(intent)
        }

    }
}
