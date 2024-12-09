package com.example.retrofitforecaster

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private val weatherAdapter = WeatherAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()
        setupRecyclerView()
        fetchWeatherData()
    }

    private fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.r_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = weatherAdapter
    }

    private fun fetchWeatherData() {
        val apiService = createApiService()
        lifecycleScope.launch {
            try {
                Log.d("MainActivity", "Запрос погоды для города: Шклов")
                val response = apiService.getWeatherForecast("Шклов", "b34d57cbea81313213c07de26a250289")

                if (response.list.isNotEmpty()) {
                    Log.d("MainActivity", "Получены данные погоды: ${response.list.size} записей")
                    weatherAdapter.submitList(response.list)
                } else {
                    Log.w("MainActivity", "Ответ от сервера пустой")
                    showToast("No data available")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Ошибка при загрузке данных: ${e.message}", e)
                showToast("Error loading data")
            }
        }
    }

    private fun createApiService(): WeatherApi {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(WeatherApi::class.java)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}