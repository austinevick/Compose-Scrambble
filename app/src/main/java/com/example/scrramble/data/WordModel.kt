package com.example.scrramble.data

import com.google.gson.annotations.SerializedName

data class WordModel(
   @SerializedName(value = "prompt") val prompt: String
)

data class ResponseModel(
   @SerializedName(value = "status") val status: Int,
   @SerializedName(value = "message") val message: String,
   @SerializedName(value = "data") val data: String,
)