package com.fevziomurtekin.sample

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fevziomurtekin.custom_mapview.View
import com.fevziomurtekin.custom_mapview.data.Place
import com.fevziomurtekin.custom_mapview.util.PlaceType
import com.google.android.gms.maps.model.LatLng

class MainActivity : View() {

    private var lists:MutableList<Place> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*Default place list adding*/
        val school:Place = Place()
        school.name="Uludag University"
        school.placeType=PlaceType.SCHOOL
        school.isUrl=false
        /*40.234583,28.8812533*/
        school.latitude=40.234583
        school.longitude=28.8812533
        school.content="Aklın ve bilimin öncülük ettiği çağdaş, demokratik, özgür düşünceli ve kişisel sorumluluk duyguları gelişmiş, toplumun inanç ve değerlerine saygılı, kültürel ve tarihi değerlerini benimsemiş, uluslararası vizyon sahibi gençler yetiştirmeyi amaç edinen üniversitemize bağlı olarak, 15 Fakülte, 2 Yüksekokul, 15 Meslek Yüksekokulu, 1 Konservatuar, 4 Enstitü, 24 Uygulama ve Araştırma Merkezi ile 1 Araştırma Merkezi ve Rektörlüğe bağlı olarak kurulan 5 bölüm bulunmaktadır.\n" +
                "\n" +
                "1970 yılında İstanbul Üniversitesi'ne bağlı olarak kurulan Bursa Tıp Fakültesi ile 1974 yılında kurulan Bursa İktisadi ve Sosyal Bilimler Fakültesi  Üniversitenin temelini oluşturmaktadır."
        school.phone="(0224) 294 0000"

        val uludag:Place = Place()
        uludag.name="Uludağ National Park"
        uludag.placeType=PlaceType.PARKING_AREA
        uludag.isUrl=true
        uludag.url="https://cdn4.vectorstock.com/i/1000x1000/22/63/mountain-icons-on-white-background-vector-21032263.jpg"
        uludag.content=""
        /*40.112148,29.0715413*/
        uludag.latitude=40.112148
        uludag.longitude=29.0715413

        val ulucamii:Place = Place()
        ulucamii.name="Ulu Camii Mosque"
        ulucamii.placeType=PlaceType.MOSQUE
        ulucamii.isUrl=false
        ulucamii.content="1395-1399 yılları arasında Yıldırım Bayezid tarafından Bursa'da yaptırılan cami, Bursa'daki mimari eserlerin en büyüğüdür. Cami kapısının üzerinde İvaz Paşa'nın adı bulunmaktadır. \n" +
                "\n" +
                "Paye ve sütunlu olan düz çatı ile örülen Selçuklu camiilerinin kubbeli düzene çevrilmiş ilk örneklerindendir. 56x68 m boyutlarındadır. 12 Paye ile, 5 nefe bölünmüştür. 20 kubbesi vardır. Üzeri açık kubbenin altında bir şadırvan vardır. Şadırvanın çevresinde Kur'an okumak için ayrılmış sofalar vardır. "
        /*40.184151, 29.061927*/
        ulucamii.latitude=40.18415
        ulucamii.longitude=29.061927

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
        lists.add(ulucamii)
        lists.add(school)
        lists.add(uludag)

        this.addPlacesList(lists)
        this.setMenuAnimation_time(350)
        this.setSearchAnimation_time(350)
        this.setFocus(LatLng(40.1122,29.061927))
        this.setDefaultSearchError("error.")


    }
}
