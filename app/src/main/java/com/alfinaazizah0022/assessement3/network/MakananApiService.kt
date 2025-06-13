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

private const val BASE_URL = "https://store.sthresearch.site/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MakananApiService {
    @GET("book.php")
    suspend fun getMakanan(
        @Header("Authorization") userId: String
    ): List<Makanan>

    @Multipart
    @POST("book.php")
    suspend fun postMakanan(
        @Header("Authorization") userId: String,
        @Part("title") nama: RequestBody,
        @Part("author") penulis: RequestBody,
        @Part("publisher") penerbit: RequestBody,
        @Part("year") year: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @DELETE("book.php")
    suspend fun deleteMakanan(
        @Header("Authorization") userId: String,
        @Query("id") id: String
    ): OpStatus

    @Multipart
    @POST("book.php")
    suspend fun updateMakanan(
        @Header("Authorization") userId: String,
        @Part("id") id: RequestBody,
        @Part("title") nama: RequestBody,
        @Part("author") penulis: RequestBody,
        @Part("publisher") penerbit: RequestBody,
        @Part("year") year: RequestBody,
        @Part image: MultipartBody.Part?
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