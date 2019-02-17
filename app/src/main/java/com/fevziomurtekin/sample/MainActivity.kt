package com.fevziomurtekin.sample

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fevziomurtekin.custom_mapview.View
import com.fevziomurtekin.custom_mapview.data.Place
import com.fevziomurtekin.custom_mapview.util.PlaceType

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

        lists.add(school)
        lists.add(uludag)

        this.addPlacesList(lists)

    }
}
