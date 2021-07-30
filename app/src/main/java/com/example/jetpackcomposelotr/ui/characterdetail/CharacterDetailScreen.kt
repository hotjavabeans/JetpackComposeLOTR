package com.example.jetpackcomposelotr.ui.characterdetail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.jetpackcomposelotr.R
import com.example.jetpackcomposelotr.data.remote.responses.Character
import com.example.jetpackcomposelotr.data.remote.responses.DocX
import com.example.jetpackcomposelotr.util.Resource

@Composable
fun CharacterDetailScreen(
    navController: NavController,
    characterId: String,
    topPadding: Dp = 20.dp,
    characterImageSize: Dp = 200.dp,
    viewModel: CharacterDetailViewModel = hiltViewModel()
) {
    val characterInfo = produceState<Resource<Character>>(initialValue = Resource.Loading()) {
        value = viewModel.getCharacterInfo(characterId = characterId) /*"5cd99d4bde30eff6ebccfbe6"*/
    }.value
    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(color = Color.Black)
            .padding(bottom = 16.dp)
    ) {
        CharacterDetailTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )
        CharacterDetailStateWrapper(
            characterInfo = characterInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + characterImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .border(border = BorderStroke(1.dp, Color.Black))
                .background(color = MaterialTheme.colors.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    top = topPadding + characterImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        )
        Box(contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()) {
            if (characterInfo is Resource.Success) {
                Image(
                    painter = rememberImagePainter(
                        R.drawable.aragorn,
                        builder = {
                            crossfade(1000)
                            transformations(CircleCropTransformation())
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(characterImageSize)
                        .offset(y = topPadding)
                )
            }
        }
    }
}

@Composable
fun CharacterDetailTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun CharacterDetailStateWrapper(
    characterInfo: Resource<Character>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when (characterInfo) {
        is Resource.Success -> {
            CharacterDetailSection(
                characterInfo = characterInfo
            )
        }
        is Resource.Error -> {
            Text(
                text = characterInfo.message!!,
                color = Color.Red,
                modifier = modifier
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier
            )
        }
    }
}

@Composable
fun CharacterDetailSection(
    characterInfo: Resource<Character>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        val data = characterInfo.data?.docs?.get(0)

        if (data != null) {
            Text(
                text = data.name,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface
            )
            CharacterRaceSection(data = data)
            CharacterDetailDataSection(data = data)

        }
    }
}

@Composable
fun CharacterRaceSection(data: DocX) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .clip(CircleShape)
                .background(color = Color.Green)
                .height(35.dp)
        ) {
            Text(
                text = data.race,
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun CharacterDetailDataSection(
    data: DocX,
    sectionHeight: Dp = 80.dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        CharacterBirthSection(
            data = data,
            dataIcon = painterResource(id = R.drawable.ic_birth),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier
            .size(1.dp, sectionHeight)
            .background(Color.LightGray))
        CharacterDeathSection(
            data = data,
            dataIcon = painterResource(id = R.drawable.ic_death),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CharacterBirthSection(data: DocX, dataIcon: Painter, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            painter = dataIcon,
            contentDescription = null,
            tint = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .size(80.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = data.birth,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun CharacterDeathSection(data: DocX, dataIcon: Painter, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            painter = dataIcon,
            contentDescription = null,
            tint = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .size(80.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = data.death,
            color = MaterialTheme.colors.onSurface
        )
    }
}


@Composable
fun CharacterGenderSection(data: DocX) {

}

