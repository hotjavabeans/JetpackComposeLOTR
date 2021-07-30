package com.example.jetpackcomposelotr.repository

import com.example.jetpackcomposelotr.data.remote.LOTRApi
import com.example.jetpackcomposelotr.data.remote.responses.Character
import com.example.jetpackcomposelotr.data.remote.responses.CharacterList
import com.example.jetpackcomposelotr.util.Constants.ACCESS_TOKEN
import com.example.jetpackcomposelotr.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class LOTRRepository @Inject constructor(
    private val api: LOTRApi
) {
    suspend fun getCharacterList(limit: Int, offset: Int): Resource<CharacterList> {
        val response = try {
            api.getCharacterList("Bearer $ACCESS_TOKEN", limit, offset)
        } catch(e: Exception) {
            return Resource.Error("An unknown error occurred. ${e.message}")
        }
        return Resource.Success(response)
    }

    suspend fun getCharacterInfo(characterId: String): Resource<Character> {
        val response = try {
            api.getCharacterInfo("Bearer $ACCESS_TOKEN", characterId)
        } catch(e: Exception) {
            return Resource.Error("An unknown error occurred. ${e.message}")
        }
        return Resource.Success(response)
    }
}