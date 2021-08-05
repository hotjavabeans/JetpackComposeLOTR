package com.example.jetpackcomposelotr.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Quote(
    @SerializedName("docs")
    val docs: List<DocXX>,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)