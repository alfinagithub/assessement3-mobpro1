package com.alfinaazizah0022.assessement3.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfinaazizah0022.assessement3.model.Makanan
import com.alfinaazizah0022.assessement3.network.ApiStatus
import com.alfinaazizah0022.assessement3.network.MakananApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Makanan>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            Log.d("MainViewModel", "Attempting to retrieve data for userId: $userId")
            try {
                data.value = MakananApi.service.getMakanan(userId)
                status.value = ApiStatus.SUCCESS
                Log.d("MainViewModel", "Data retrieval SUCCESS. Total items: " +
                        "${data.value.size}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Data retrieval FAILED: ${e.message}", e)
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: String, title: String, author: String, publisher: String, year: String,
                 bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = MakananApi.service.postMakanan(
                    userId,
                    title.toRequestBody("text/plain".toMediaTypeOrNull()),
                    author.toRequestBody("text/plain".toMediaTypeOrNull()),
                    publisher.toRequestBody("text/plain".toMediaTypeOrNull()),
                    year.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if (result.status == "success")
                    retrieveData(userId)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun updateData(userId: String, id: String, title: String, author: String, publisher: String,
                   year: String, bitmap: Bitmap?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imagePart = bitmap?.toMultipartBody()
                val result = MakananApi.service.updateMakanan(
                    userId,
                    id.toRequestBody("text/plain".toMediaTypeOrNull()),
                    title.toRequestBody("text/plain".toMediaTypeOrNull()),
                    author.toRequestBody("text/plain".toMediaTypeOrNull()),
                    publisher.toRequestBody("text/plain".toMediaTypeOrNull()),
                    year.toRequestBody("text/plain".toMediaTypeOrNull()),
                    imagePart
                )

                if (result.status == "success") {
                    Log.d("MainViewModel", "Update successful, retrieving data...")
                    retrieveData(userId)
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }


    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody)
    }

    fun deleteMakanan(userId: String, id: String) {
        viewModelScope.launch {
            try {
                val result = MakananApi.service.deleteMakanan(
                    userId, id
                )

                if (result.status == "success") {
                    retrieveData(userId)
                } else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun clearMessage() { errorMessage.value = null }
}