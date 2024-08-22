package com.aman.tomtomimplementation.retro


import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlacesApiResponse(
    @Json(name = "results")
    val results: List<Result?>? = listOf(),
    @Json(name = "summary")
    val summary: Summary? = Summary()
)

@JsonClass(generateAdapter = true)
data class Result(
    @Json(name = "address")
    val address: Address? = Address(),
    @Json(name = "dist")
    val dist: Double? = 0.0, // 7.428197
    @Json(name = "entryPoints")
    val entryPoints: List<EntryPoint?>? = listOf(),
    @Json(name = "id")
    val id: String? = "", // mDSqx3zq4FDCg6PMl3bYXg
    @Json(name = "info")
    val info: String? = "", // search:ta:356009033054543-IN
    @Json(name = "poi")
    val poi: Poi? = Poi(),
    @Json(name = "position")
    val position: Position? = Position(),
    @Json(name = "score")
    val score: Double? = 0.0, // 99.9925765991
    @Json(name = "type")
    val type: String? = "", // POI
    @Json(name = "viewport")
    val viewport: Viewport? = Viewport()
)

@JsonClass(generateAdapter = true)
data class Address(
    @Json(name = "country")
    val country: String? = "", // India
    @Json(name = "countryCode")
    val countryCode: String? = "", // IN
    @Json(name = "countryCodeISO3")
    val countryCodeISO3: String? = "", // IND
    @Json(name = "countrySecondarySubdivision")
    val countrySecondarySubdivision: String? = "", // Bengaluru
    @Json(name = "countrySubdivision")
    val countrySubdivision: String? = "", // Karnataka
    @Json(name = "countrySubdivisionCode")
    val countrySubdivisionCode: String? = "", // KA
    @Json(name = "countrySubdivisionName")
    val countrySubdivisionName: String? = "", // Karnataka
    @Json(name = "freeformAddress")
    val freeformAddress: String? = "", // 7A, Sarjapur Road, Rainbow Residency, Halanayakana Halli, Bengaluru 560035, Karnataka
    @Json(name = "localName")
    val localName: String? = "", // Bengaluru
    @Json(name = "municipality")
    val municipality: String? = "", // Bengaluru
    @Json(name = "municipalitySecondarySubdivision")
    val municipalitySecondarySubdivision: String? = "", // Rainbow Residency
    @Json(name = "municipalitySubdivision")
    val municipalitySubdivision: String? = "", // Halanayakana Halli
    @Json(name = "postalCode")
    val postalCode: String? = "", // 560035
    @Json(name = "streetName")
    val streetName: String? = "", // Sarjapur Road
    @Json(name = "streetNumber")
    val streetNumber: String? = "" // 7A
)
{
    override fun toString(): String {
        return "$streetNumber $streetName $postalCode "
    }
}

@JsonClass(generateAdapter = true)
data class EntryPoint(
    @Json(name = "position")
    val position: Position? = Position(),
    @Json(name = "type")
    val type: String? = "" // main
)


@JsonClass(generateAdapter = true)
data class Poi(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    @Json(name = "categories")
    val categories: List<String?>? = listOf(),
    @Json(name = "categorySet")
    val categorySet: List<CategorySet?>? = listOf(),
    @Json(name = "classifications")
    val classifications: List<Classification?>? = listOf(),
    @Json(name = "name")
    val name: String? = "", // Sri Sai Venkateswara Pg For Gents
    @Json(name = "phone")
    val phone: String? = "" // +91 96869 88575
)

@JsonClass(generateAdapter = true)
data class CategorySet(
    @Json(name = "id")
    val id: Int? = 0 // 7314004
)

@JsonClass(generateAdapter = true)
data class Classification(
    @Json(name = "code")
    val code: String? = "", // HOTEL_MOTEL
    @Json(name = "names")
    val names: List<Name?>? = listOf()
)

@JsonClass(generateAdapter = true)
data class Name(
    @Json(name = "name")
    val name: String? = "", // hostel
    @Json(name = "nameLocale")
    val nameLocale: String? = "" // en-US
)


@JsonClass(generateAdapter = true)
data class Position(
    @Json(name = "lat")
    val lat: Double? = 0.0, // 12.910856
    @Json(name = "lon")
    val lon: Double? = 0.0 // 77.680565
)

@JsonClass(generateAdapter = true)
data class Viewport(
    @Json(name = "btmRightPoint")
    val btmRightPoint: BtmRightPoint? = BtmRightPoint(),
    @Json(name = "topLeftPoint")
    val topLeftPoint: TopLeftPoint? = TopLeftPoint()
)

@JsonClass(generateAdapter = true)
data class BtmRightPoint(
    @Json(name = "lat")
    val lat: Double? = 0.0, // 12.90933
    @Json(name = "lon")
    val lon: Double? = 0.0 // 77.68213
)

@JsonClass(generateAdapter = true)
data class TopLeftPoint(
    @Json(name = "lat")
    val lat: Double? = 0.0, // 12.91238
    @Json(name = "lon")
    val lon: Double? = 0.0 // 77.679
)


@JsonClass(generateAdapter = true)
data class Summary(
    @Json(name = "fuzzyLevel")
    val fuzzyLevel: Int? = 0, // 1
    @Json(name = "geoBias")
    val geoBias: GeoBias? = GeoBias(),
    @Json(name = "numResults")
    val numResults: Int? = 0, // 1
    @Json(name = "offset")
    val offset: Int? = 0, // 0
    @Json(name = "queryTime")
    val queryTime: Int? = 0, // 13
    @Json(name = "queryType")
    val queryType: String? = "", // NEARBY
    @Json(name = "totalResults")
    val totalResults: Int? = 0 // 1
)

@JsonClass(generateAdapter = true)
data class GeoBias(
    @Json(name = "lat")
    val lat: Double? = 0.0, // 12.9109085
    @Json(name = "lon")
    val lon: Double? = 0.0 // 77.6806073
)
    
