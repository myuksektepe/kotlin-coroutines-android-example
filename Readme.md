# Kotlin Coroutines

<img src="https://yuklio.com/f/qSXgG-screenshot_20211124_003524_com.muratlakodla.kotlin_coroutines.jpg" alt="Screenshot" width="300"/>


Kotlin Coroutines: Senkron kod yazarak asenkron uygulamalar geliştirmeye yarayan bir eklentidir.


Network istekleri cevap gelene kadar bulunduğu thread'i bekletme moduna alır ve sonraki işlemleri bloklar. 

Main thread (ana işparçası) içerisinde yapılan network istekleri, Main thread'i blokladığı için uygulama;
UI işlemlerini gerçekleştirecek fonksiyonları çağıramaz. 

Eğer network istek işlemi uzun sürer ve bir şekilde başarısız olursa bu durum uygulama yanıt vermiyor 
(Application Not Responding - ANR) hatalarına sebep olacaktır.


Bu sorunu ortadan kaldırmak ve daha iyi bir kullanıcı deneyimi sağlamak için network istekleri gibi işlemleri 
Main thread yerine background'ta yani arkaplanda yapmamız gerekir.

Ve tabii ki arkaplanda yapılan bu operasyonların sonuçlarını Main thread'e bildirmemiz gerekecektir.

Bu örneğimizde bu işlemler için ViewModel ve LiveData yapılarını kullanacağız. 


Detaylı açıklamaları MainActivityViewModel ve MainActivity içerisinde bulabilirsiniz.



Kullandığımız API: https://api2.binance.com/api/v3/ticker/24hr
Kotlin Coroutine kullanmadan yapılan istekler için istatistikler şu şekilde;


2021-11-24 00:03:16.098/applog: sendGetWithHttpURLConnection -> started 

2021-11-24 00:03:18.417/applog: sendGetWithHttpURLConnection -> finished

2.4 sn boyunca Main Thread bloklanıyor.

-

2021-11-24 00:03:18.469/applog: sendGetWithOkHttp -> started 

2021-11-24 00:03:20.654/applog: sendGetWithOkHttp -> finished

2.2 sn boyunca Main Thread bloklanıyor.