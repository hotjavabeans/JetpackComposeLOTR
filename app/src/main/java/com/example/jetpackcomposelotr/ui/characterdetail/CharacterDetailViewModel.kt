package com.example.jetpackcomposelotr.ui.characterdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.jetpackcomposelotr.data.db.RoomEntity
import com.example.jetpackcomposelotr.data.remote.responses.Character
import com.example.jetpackcomposelotr.data.remote.responses.Quote
import com.example.jetpackcomposelotr.repository.LOTRRepository
import com.example.jetpackcomposelotr.repository.RoomRepository
import com.example.jetpackcomposelotr.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val repository: LOTRRepository,
) : ViewModel() {

    suspend fun getCharacterInfo(characterId: String): Resource<Character> {
        return repository.getCharacterInfo(characterId = characterId)
    }

    suspend fun getCharacterQuote(characterId: String): Resource<Quote> {
        return repository.getCharacterQuote(characterId = characterId)
    }
}