package com.android.AnisWeatherApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import com.ndroid.weatherapp2.R

class MainActivity : AppCompatActivity() {

    private lateinit var editCityName: EditText
    private lateinit var btnSearch: Button
    private lateinit var imageWeather: ImageView
    private lateinit var tvTemperature: TextView
    private lateinit var tvCityName: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvWind: TextView

    private lateinit var layoutWeather: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editCityName = findViewById(R.id.editCity)
        btnSearch = findViewById(R.id.btnSearch)
        imageWeather = findViewById(R.id.imageWeather)
        tvCityName = findViewById(R.id.tvCityName)
        tvTemperature = findViewById(R.id.tvTemperature)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvWind = findViewById(R.id.tvWind)
        layoutWeather = findViewById(R.id.layoutWeather)

        btnSearch.setOnClickListener {
            val city = editCityName.text.toString()
            if (city.isEmpty()) {
                Toast.makeText(this, "Type the City Name", Toast.LENGTH_SHORT).show()
            } else {
                getWeatherByCity(city)
            }
        }
    }

    private fun getWeatherByCity(city: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(WeatherService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherService = retrofit.create(WeatherService::class.java)

        val result = weatherService.getWeatherByCity(city)

        result.enqueue(object : Callback<WeatherResult> {
            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                if (response.isSuccessful) {
                    val result = response.body()

                    tvTemperature.text = "${result?.main?.temp} °C"
                    tvCityName.text = result?.name
                    tvHumidity.text = "Humidity: ${result?.main?.humidity}%"
                    tvWind.text = "Wind: ${result?.wind?.speed} m/s, ${result?.wind?.deg}°"

                    Picasso.get().load("https://openweathermap.org/img/w/${result?.weather?.get(0)?.icon}.png").into(imageWeather)


                    updateWindImage(result?.wind?.speed ?: 0.0)

                    layoutWeather.visibility = View.VISIBLE

                    setAppBackground(result?.main?.temp ?: 0.0)
                }
            }



            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAppBackground(temperature: Double) {
        val mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
        val backgroundResId: Int = when {
            temperature > 20.0 -> R.drawable.hot_background
            temperature < 10.0 -> R.drawable.cold_background
            else -> R.drawable.default_background
        }
        mainLayout.setBackgroundResource(backgroundResId)
    }

    private fun updateWindImage(windDegree: Double) {
        val imageWind = findViewById<ImageView>(R.id.imageWind)

        val windImageResource = when {
            windDegree >= 0 || windDegree < 90 -> R.drawable.northarrow
            windDegree >= 90 && windDegree < 180 -> R.drawable.eastarrow
            windDegree >= 180 && windDegree < 270 -> R.drawable.sudarrow
            else -> R.drawable.westarrow

        }

        imageWind.setImageResource(windImageResource)
    }



}
