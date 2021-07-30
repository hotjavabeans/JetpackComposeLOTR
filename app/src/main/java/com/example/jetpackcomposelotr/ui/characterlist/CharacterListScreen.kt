package com.example.jetpackcomposelotr.ui.characterlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpackcomposelotr.R
import com.example.jetpackcomposelotr.Screen
import com.example.jetpackcomposelotr.data.models.CharacterListEntry

@Composable
fun CharacterListScreen(
    navController: NavController,
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    Surface(
        color = Color.Black,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.lotr_heading),
                contentDescription = "Lord of the Rings logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
            SearchBar(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ) {
                viewModel.searchCharacterList(it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            CharacterList(navController = navController)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "Search...",
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    
    Box(modifier = modifier) {
        TextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            placeholder = {
                Text(text = hint)          
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .padding(horizontal = 3.dp, vertical = 5.dp),
            keyboardOptions = if (text.isNotEmpty()) {
                KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                )
            } else {
                   KeyboardOptions(imeAction = ImeAction.Done)
            }

            ,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                textColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                placeholderColor = Color.Black
            ),
            shape = CircleShape
        )
    }
}

@Composable
fun CharacterList(
    navController: NavController,
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    val characterList by remember { viewModel.characterList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val isSearching by remember { viewModel.isSearching }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount = if(characterList.size % 2 == 0) {
            characterList.size / 2
        } else {
            characterList.size / 2 + 1
        }
        items(itemCount) {
            if(it >= itemCount - 1 && !endReached && !isLoading && !isSearching) {
                viewModel.loadCharactersPaginated()
            }
            CharacterRow(rowIndex = it, entries = characterList, navController = navController)
        }
    }

    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if(isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if(loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadCharactersPaginated()
            }
        }
    }
}

@Composable
fun CharacterEntry(
    entry: CharacterListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    Box(
        contentAlignment = Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.White,
                        Color.Blue,
                    )
                )
            )
            .clickable {
                navController.navigate(
                    Screen.CharacterDetailScreen.route + "/${entry.id}" /*/${entry.characterName}/${entry.race}*/
                )
            }
    ) {
        Column {
            Text(
                text = entry.characterName,
                fontFamily = FontFamily.Default,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = entry.race,
                fontFamily = FontFamily.Default,
                fontSize = 20.sp,

                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CharacterRow(
    rowIndex: Int,
    entries: List<CharacterListEntry>,
    navController: NavController
) {
    Column {
        Row {
            CharacterEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if(entries.size >= rowIndex * 2 + 2) {
                CharacterEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}











