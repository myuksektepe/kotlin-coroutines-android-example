package com.muratlakodla.kotlin_coroutines.view

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.muratlakodla.kotlin_coroutines.R
import com.muratlakodla.kotlin_coroutines.databinding.ActivityMainBinding
import com.muratlakodla.kotlin_coroutines.model.Result
import com.muratlakodla.kotlin_coroutines.viewmodel.MainActivityViewModel
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_main, null, false)
        setContentView(binding.root)

        // Send HTTP Request With Coroutine
        binding.btnSendHTTPRequestWithCoroutine.setOnClickListener {
            apiCall()
        }

        // Send HTTP Request With Coroutine (HttpURLConnection)
        binding.btnSendHTTPRequestWithoutCoroutine1.setOnClickListener {
            apiCall1()
        }

        // Send HTTP Request With Coroutine (OkHttp)
        binding.btnSendHTTPRequestWithoutCoroutine2.setOnClickListener {
            apiCall2()
        }

    }

    private fun apiCall() {
        viewModel.fetchAllTickers()
        viewModel.tickerList.observe(this, {
            it.let {
                when (it) {
                    is Result.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                    }
                    is Result.Error -> {
                        binding.textMain.text = it.exception.message
                        binding.loading.visibility = View.GONE
                    }
                    is Result.Success -> {
                        binding.textMain.text = it.data[3].toString()
                        binding.loading.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun apiCall1() {

        binding.loading.visibility = View.VISIBLE
        binding.textMain.text = ""
        /**
         * İşlem başlatıldığında Loading progress'i gösterip istek cevabının gösterildiği
         * TextView'i temizlemesi gerekiyor ama Main Thread bloklandığı için bu işlemleri yapamayacak.
         */


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                val response = viewModel.sendGetWithHttpURLConnection()
                if (!response.isNullOrEmpty()) {
                    val jsonArray = JSONArray(response)
                    val jsonObject = jsonArray[0]
                    binding.textMain.text = jsonObject.toString()
                }
            } catch (e: Exception) {
                binding.textMain.text = e.message.toString()
            }
        }

        // Hide Loading
        binding.loading.visibility = View.GONE

    }

    private fun apiCall2() {

        binding.loading.visibility = View.VISIBLE
        binding.textMain.text = ""
        /**
         * İşlem başlatıldığında Loading progress'i gösterip istek cevabının gösterildiği
         * TextView'i temizlemesi gerekiyor ama Main Thread bloklandığı için bu işlemleri yapamayacak.
         */

        val response2 = viewModel.sendGetWithOkHttp()
        if (!response2.isNullOrEmpty()) {
            try {
                val jsonArray = JSONArray(response2)
                val jsonObject = jsonArray[1]
                binding.textMain.text = jsonObject.toString()
            } catch (e: Exception) {
                binding.textMain.text = e.message.toString()
            }
        }

        // Hide Loading
        binding.loading.visibility = View.GONE

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}