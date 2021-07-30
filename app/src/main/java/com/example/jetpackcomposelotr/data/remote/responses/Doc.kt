package com.example.jetpackcomposelotr.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Doc(
    @SerializedName("birth")
    val birth: String,
    @SerializedName("death")
    val death: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("hair")
    val hair: String,
    @SerializedName("height")
    val height: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("race")
    val race: String,
    @SerializedName("realm")
    val realm: String,
    @SerializedName("spouse")
    val spouse: String,
    @SerializedName("wikiUrl")
    val wikiUrl: String
)