package com.example.jetpackcomposelotr.data.remote.responses


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CURRENT_CHARACTER_ID = 0

@Entity
data class Character(
    @SerializedName("docs")
    val docs: List<DocX>,
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
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = CURRENT_CHARACTER_ID
}