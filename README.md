## Custom MapView
🗺️ A customized Android library made using Google map.

<p align="center">
<img align="center" src="/art/map.gif" width="300" height="450" />
</p>

# Usage

- You must enter your Google Map key in Android Manifest.xml.
```Gradle
  <meta-data
                android:name="com.google.android.maps.v2.API_KEY"
                android:value="@string/google_maps_key" /
  ```
  
  - Include the necessary libraries in app->build.gradle  .
		
```Gradle
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  .....
    dependencies {
	    implementation 'com.github.fevziomurtekin:custom-mapview:0.1.1'
	    implementation 'com.google.android.gms:play-services-maps:16.1.0'
	  }
	}
  ```

- Include in the activity 

 ```Gradle 
 class MainActivity : View() {

    private var lists:MutableList<Place> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*Default place list adding*/
        val anitkabir:Place = Place()
        anitkabir.name="Anıtkabir"
        anitkabir.placeType=PlaceType.HEART
        anitkabir.isUrl=false
        anitkabir.content="Atatürk bir devri açıp yeni bir düzeni getirdikten sonra 10 Kasım 1938'de öldüğünde, geçici olarak Etnografya Müzesi'ne defnedildi. Uygun bir anıt yeri aranmasına hemen başlandı ve Ankara'nın kente egemen bir tepesi olan Rasattepe uygun görüldü. Anıtkabir'in projesi bir yarışmayla belirlendi. Bu amaçla açılan yarışmada Emin Onat ve Orhan Arda'nın projesi başarılı görülerek uygulanmasına karar verildi.\n" +
                "\n" +
                "Anıtkabir bugünkü adı Anıttepe olan yerde 15 bin metrekarelik bir alanda yapıldı. Yapımında Çankırı'nın açık sarı ve gri travertenleri kullanıldı.\n" +
                "\n" +
                "Anıtkabir bütün Türklerin sevgisinin, saygısının bütünleştiği bir simgedir. Başlangıç noktasındaki İstiklal ve Hürriyet kulelerinden, iki yanında Hüseyin Özkan'ın 24 Hitit aslanının yer aldığı Aslanlı Yoldan, Mehmetçik, Müdafaa-i Hukuk, Cumhuriyet, Zafer, İnkılap, Misak-ı Milli, Barış ve 23 Nisan kulelerinin çepeçevre sarmaladığı Zafer Alanı'na kadar her nokta, Türk'ün verdiği bağımsızlık ve özgürlük savaşını anlatır. 32 x 60 metre boyutlarında ve 20 metre yüksekliğinde, duvarları ve döşemesi koyu renkli Bilecik mermerleriyle kaplı, tavanı altın mozaikle süslü Büyük Salon'un gizemli havası ise insanı, acılarla, gözyaşlarıyla dolu savaş yıllarına götürür. Salonun giriş kapısının karşısında, penceresi Ankara Kalesi'ne bakan duvarın önündeki taş bir set üstünde tek parça mermerden oluşan simgesel bir lahit vardır. Atatürk'ün naaşı, lahdin tam altındaki toprak mezardadır."
        /*39.925276, 32.836955*/
        anitkabir.latitude=39.925276
        anitkabir.longitude=32.836955

        lists.add(anitkabir)
        this.addPlacesList(lists)
        this.setMenuAnimation_time(350)  
        this.setSearchAnimation_time(350)
	    this.setFocus(LatLng(40.1122,29.061927))
        this.setDefaultSearchError("error.")

    }
}
  ```
  
# Attributes

| Attribute | Description |
| --- | --- |
| `PlaceList` | List of places to add on the map. " |
| `menuAnimation_time` | The time in int of the menu animation time (by default 300 ms) |
| `searchAnimation_time` | The time in int of the search animation time (by default 300 ms) |
| `defauiltSearchError` | The value in string of the message items (by default "The location you were looking for was not found on the map.")  |
| `focus` | The location in LatLng of the location (by default 39.92077,32.85411) |

> To add new places to the map, a list derived from the "Palace" class must be submitted.

| Key | Value |
| --- | --- |
| `name` | The name in string of marker snippet. " |
| `content` | The name in string of content on the page that opens when you clicked. |
| `latitude` | The name in double of marker position-latitude. (by default 0.0) |
| `longitude` |  The name in double of marker position-longitude. (by default 0.0) |
| `placeType` | RESTUARANT, HOTEL, BAR, BANK, GAS_STATION, PARKING_AREA,STORE, HOSPITAL, COFFEE, SCHOOL, MUSEUM , MALL,MOSQUE,STADIUM, HEART, OTHER |
| `url` | The url in string of the Place-url (by default "") |
| `resource` | The resource in Int of the resource (by default -1. eg. R.drawable.map) |
| `phone` | The phone number in string of the Place-number (by default "" ) |

> If you leave the url and resource sections blank, we will use the icons we give automatically.

## License
The Apache License 2.0 - see [`LICENSE`](LICENSE) for more details
