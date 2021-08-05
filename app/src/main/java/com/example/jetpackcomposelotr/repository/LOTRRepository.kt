package com.example.jetpackcomposelotr.repository

import com.example.jetpackcomposelotr.data.remote.LOTRApi
import com.example.jetpackcomposelotr.data.remote.responses.Character
import com.example.jetpackcomposelotr.data.remote.responses.CharacterList
import com.example.jetpackcomposelotr.data.remote.responses.Quote
import com.example.jetpackcomposelotr.util.Constants.ACCESS_TOKEN
import com.example.jetpackcomposelotr.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class LOTRRepository @Inject constructor(
    private val api: LOTRApi
) {
    suspend fun getCharacterList(): Resource<CharacterList> {
        val response = try {
            api.getCharacterList("Bearer $ACCESS_TOKEN")
        } catch (e: Exception) {
            return Resource.Error(
                "Unable to retrieve unpaginated list. ${e.message}"
            )
        }
        return Resource.Success(response)
    }

    suspend fun getCharacterList(limit: Int, offset: Int): Resource<CharacterList> {
        val response = try {
            api.getCharacterList("Bearer $ACCESS_TOKEN", limit, offset)
        } catch(e: Exception) {
            return Resource.Error(
                "An unknown error occurred retrieving character list. ${e.message}"
            )
        }
        return Resource.Success(response)
    }

    suspend fun getCharacterInfo(characterId: String): Resource<Character> {
        val response = try {
            api.getCharacterInfo("Bearer $ACCESS_TOKEN", characterId)
        } catch(e: Exception) {
            return Resource.Error(
                "An unknown error occurred retrieving character info. ${e.message}"
            )
        }
        return Resource.Success(response)
    }

    suspend fun getCharacterQuote(characterId: String): Resource<Quote> {
        val response = try {
            api.getCharacterQuote("Bearer $ACCESS_TOKEN", characterId)
        } catch (e: Exception) {
            return Resource.Error(
                "An error occurred retrieving quotes. ${e.message}"
            )
        }
        return Resource.Success(response)
    }
}