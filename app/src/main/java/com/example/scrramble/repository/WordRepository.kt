package com.example.scrramble.repository

import com.example.scrramble.data.ResponseModel
import com.example.scrramble.data.WordModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface WordRepository {

    @POST("generate")
   suspend fun getWordResponse(@Body prompt: WordModel): Response<ResponseModel>

}