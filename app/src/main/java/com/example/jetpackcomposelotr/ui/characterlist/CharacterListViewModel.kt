package com.example.jetpackcomposelotr.ui.characterlist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposelotr.data.models.CharacterListEntry
import com.example.jetpackcomposelotr.repository.LOTRRepository
import com.example.jetpackcomposelotr.util.Constants.PAGE_SIZE
import com.example.jetpackcomposelotr.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: LOTRRepository,
) : ViewModel() {

    private var curPage = 0

    var characterList = mutableStateOf<List<CharacterListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedCharacterList = listOf<CharacterListEntry>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init {
        loadCharacters()
    }

    fun searchCharacterList(query: String) {
        val listToSearch = if (isSearchStarting) {
            characterList.value
        } else {
            cachedCharacterList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                characterList.value = cachedCharacterList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.characterName.contains(query.trim(), ignoreCase = true) ||
                        it.race.contains(query.trim(), ignoreCase = true)
            }
            if (isSearchStarting) {
                cachedCharacterList = characterList.value
                isSearchStarting = false
            }
            characterList.value = results
            isSearching.value = true
        }
    }

    fun loadCharacters() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getCharacterList()
            when (result) {
                is Resource.Success -> {
                    val characterListEntries = result.data!!.docs.mapIndexed { index, entry ->
                        val characterName = entry.name
                        val race = entry.race
                        val id = entry.id
                        CharacterListEntry(
                            characterName = characterName,
                            race = race,
                            id = id
                        )
                    }
                    loadError.value = ""
                    isLoading.value = false
                    characterList.value += characterListEntries


                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                is Resource.Loading -> {
                    isLoading.value = true
                }
            }
        }
    }

    fun loadCharactersPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getCharacterList(PAGE_SIZE, curPage * PAGE_SIZE)
            when(result) {
                is Resource.Success -> {
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.total
                    val characterListEntries = result.data.docs.mapIndexed { index, entry ->
                        val characterName = entry.name
                        val race = entry.race
                        val id = entry.id
                        CharacterListEntry(
                            characterName = characterName,
                            race = race,
                            id = id
                            )
                    }
                    curPage++
                    loadError.value = ""
                    isLoading.value = false
                    characterList.value += characterListEntries
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                is Resource.Loading -> {
                    isLoading.value = true
                }
            }
        }
    }
}