package com.muratlakodla.kotlin_coroutines.viewmodel

import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muratlakodla.kotlin_coroutines.model.Result
import com.muratlakodla.kotlin_coroutines.model.TickerModel
import com.muratlakodla.kotlin_coroutines.retrofit.RetrofitService
import com.muratlakodla.kotlin_coroutines.utils.Constants.BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.HttpURLConnection
import java.net.URL


class MainActivityViewModel : ViewModel() {

    private val retrofitService = RetrofitService.retrofitService

    private val _tickerList: MutableLiveData<Result<MutableList<TickerModel>>> = MutableLiveData()
    val tickerList get() = _tickerList


    fun fetchAllTickers() {
        /**
         * Network istekleri barındıran fonksiyonlar çağırıldıkları thread'ler bloklarlar.
         *
         * Bir fonksiyonu güvenli (main-safety) olarak kabul edebilmemiz için o fonksiyonun
         * main thread'i (ana iş parçacığını) bloklamaması gerekir.
         *
         * Network isteği barındıran bu fonksiyonu güvenli hale getirmek için coroutines (eşyordam)
         * kütüphanesinin bize sağladığı withContext fonksiyonunu kullanarak işlemleri Main thread'in
         * dışına I/O iş parçacığına taşıyalım.
         *
         * Coroutine asenkron yapıda olduğu için içinde tetiklenen fonksiyonda "suspended" yani durdurulabilir,
         * başlatılabilir bir yapıda olmalıdır.
         */

        /**
         * ViewModel içinde bulunan getUsersByPageNumber fonksiyonunu yeni bir yordam
         * oluşturup UI theard'inin dışına taşıyalım.
         *
         * viewModelScope: ViewModel KTX eklentisinde önceden tanımlanmış bir CoroutineScope'udur.
         *                 Bütün coroutine (eşyordamlar) bir scope (kapsam)'a ihtiyaç duyar.
         *                 Bir CoroutineScope, bir veya daha fazla ilgili eşyordamı yönetir.
         *
         *
         * launch: Coroutine (eşyordam)'ı oluşturan ve işlevi yürütülmek üzere yönlendirciye gönderen
         *         fonksiyondur.
         *
         *
         * Dispatchers.IO: Öncesinde oluşturulan coroutine (eşyordam)'ın Giriş/Çıkış (In/Out) için ayrılmış olan
         *                 iş parçacığı üzerinde yapılması gerektiğini bildirir.
         */

        Log.i("applog", "fetchAllTickers -> started")

        viewModelScope.launch(Dispatchers.IO) {
            _tickerList.postValue(Result.Loading())
            try {
                val apiResult = retrofitService.getTickers()
                _tickerList.postValue(Result.Success(apiResult))
            } catch (e: Exception) {
                _tickerList.postValue(Result.Error(e))
            }
        }

        Log.i("applog", "fetchAllTickers -> finished")
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun sendGetWithHttpURLConnection(): String {
        Log.i("applog", "sendGetWithHttpURLConnection -> started")

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val url = URL("${BASE_URL}ticker/24hr")
        var response = ""

        try {
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                inputStream.bufferedReader().use {
                    it.lines().forEach { line ->
                        response += line.toString()
                    }
                }
                Log.i(
                    "applog",
                    "sendGetWithHttpURLConnection -> \nURL: $url\nResponse Code: $responseCode\nResponse Body: ${response}"
                )
            }
        } catch (e: Exception) {
            Log.e("applog", "sendGetWithHttpURLConnection -> Exception: ${e}")
            response = e.message.toString()
        }

        Log.i("applog", "sendGetWithHttpURLConnection -> finished")

        return response
    }

    fun sendGetWithOkHttp(): String {

        Log.i("applog", "sendGetWithOkHttp -> started")

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        var responseBody = ""
        val url = "${BASE_URL}ticker/24hr"
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
            val response = client.newCall(request).execute()
            responseBody = response.body()?.string().toString()
            Log.i(
                "applog",
                "sendGetWithOkHttp -> \nURL: $url\nResponse Code: ${response.code()}\nResponse Body: ${responseBody}"
            )
        } catch (e: Exception) {
            Log.e("applog", "sendGetWithOkHttp -> Exception: ${e}")
            responseBody = e.message.toString()
        }

        Log.i("applog", "sendGetWithOkHttp -> finished")

        return responseBody
    }
}