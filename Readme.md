# Kotlin Coroutines

Kotlin Coroutines: Senkron kod yazarak asenkron uygulamalar geliştirmeye yarayan bir eklentidir.

Main thread (ana işparçası) içerisinde yapılan network istekleri, cevap gelene kadar main thread'i bekletme moduna alır ve
sonraki işlemleri bloklar. Main thread bloklandığı zaman uygulama UI işlemlerini gerçekleştirecek fonksiyonları çağıramaz.
Bu durum genelde uygulama yanıt vermiyor (Application Not Responding - ANR) hatalarına sebep olur.

Bu sorunu ortadan kaldırmak ve daha iyi bir kullanıcı deneyimi sağlamak için network istekleri gibi işlemleri
Main thread yerine background'ta yani arkaplanda yaptırmalıyız.

Ve tabii ki arkaplanda yapılan bu operasyonların sonuçlarını Main thread'e bildirmemiz gerekir.


Bu işlemler için ViewModel yapısını kullanacağız. ViewModel içerisine yazdığımız fonksiyonlar network isteklerini yaparken
repository'leri kullanacaklar.



2021-11-24 00:03:16.098 31776-31776/com.muratlakodla.kotlin_coroutines I/applog: sendGet -> started
2021-11-24 00:03:18.417 31776-31776/com.muratlakodla.kotlin_coroutines I/applog: sendGet ->
URL: https://api2.binance.com/api/v3/ticker/24hr
Response Code: 200
Response Body: [{"symbol":"ETHBTC",...}]
2021-11-24 00:03:18.417 31776-31776/com.muratlakodla.kotlin_coroutines I/applog: sendGet -> finished

2.4 sn boyunca Main Thread bloklanıyor.



2021-11-24 00:03:18.469 31776-31776/com.muratlakodla.kotlin_coroutines I/applog: sendGetWithOkHttp -> started
2021-11-24 00:03:20.653 31776-31776/com.muratlakodla.kotlin_coroutines I/applog: sendGetWithOkHttp ->
URL: https://api2.binance.com/api/v3/ticker/24hr
Response Code: 200
Response Body: [{"symbol":"ETHBTC",...}]
2021-11-24 00:03:20.654 31776-31776/com.muratlakodla.kotlin_coroutines I/applog: sendGetWithOkHttp -> finished

2.2 sn boyunca Main Thread bloklanıyor.