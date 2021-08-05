package com.example.jetpackcomposelotr.data.remote.responses


import com.google.gson.annotations.SerializedName

data class DocXX(
    @SerializedName("character")
    val character: String,
    @SerializedName("dialog")
    val dialog: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("movie")
    val movie: String
)