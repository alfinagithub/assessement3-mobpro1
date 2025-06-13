package com.alfinaazizah0022.assessement3.network

import com.alfinaazizah0022.assessement3.model.Makanan
import com.alfinaazizah0022.assessement3.model.OpStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

private const val BASE_URL = "https://gh.d3ifcool.org/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MakananApiService {
    @GET("hewan.php")
    suspend fun getMakanan(
        @Header("Authorization") userId: String
    ): List<Makanan>

    @Multipart
    @POST("hewan.php")
    suspend fun postMakanan(
        @Header("Authorization") userId: String,
        @Part("nama") nama: RequestBody,
        @Part("namaLatin") namaLatin: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @DELETE("hewan.php")
    suspend fun deleteMakanan(
        @Header("Authorization") userId: String,
        @Query("id") id: String
    ): OpStatus
}

object MakananApi {
    val service: MakananApiService by lazy {
        retrofit.create(MakananApiService::class.java)
    }

    fun getMakananUrl(imageId: String): String {
        return "${BASE_URL}image.php?id=$imageId"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }