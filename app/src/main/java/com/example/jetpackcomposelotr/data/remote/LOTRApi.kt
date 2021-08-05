package com.example.jetpackcomposelotr.data.remote

import com.example.jetpackcomposelotr.data.remote.responses.CharacterList
import com.example.jetpackcomposelotr.data.remote.responses.Character
import com.example.jetpackcomposelotr.data.remote.responses.Quote
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.Character as JavaLangCharacter


interface LOTRApi {

    @GET("character")
    suspend fun getCharacterList(
        @Header("Authorization") token: String
    ): CharacterList

    @GET("character")
    suspend fun getCharacterList(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): CharacterList

    @GET("character/{id}")
    suspend fun getCharacterInfo(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Character

    @GET("character/{id}/quote")
    suspend fun getCharacterQuote(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Quote
}