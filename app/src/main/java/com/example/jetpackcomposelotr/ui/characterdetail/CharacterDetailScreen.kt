package com.example.jetpackcomposelotr.ui.characterdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.AndroidUriHandler
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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
import com.example.jetpackcomposelotr.data.remote.responses.Quote
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
    val characterRace = characterInfo.data?.docs?.get(0)?.race
    val characterQuote = produceState<Resource<Quote>>(initialValue = Resource.Loading()) {
        value = viewModel.getCharacterQuote(characterId = characterId)
    }.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = when (characterRace) {
                    "Human" -> {
                        Color(96, 155, 255)
                    }
                    "Elf" -> {
                        Color(5, 131, 7)
                    }
                    "Dwarf" -> {
                        Color(255, 215, 0)
                    }
                    "Hobbit" -> {
                        Color(84, 39, 39)
                    }
                    else -> {
                        Color.Transparent
                    }
                }
            )
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
            characterQuote = characterQuote,
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
    characterQuote: Resource<Quote>,
    characterInfo: Resource<Character>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when (characterInfo) {
        is Resource.Success -> {
            CharacterDetailSection(
                characterQuote = characterQuote,
                characterInfo = characterInfo,
                modifier = modifier
                    .offset(y = (-20).dp)
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
    characterQuote: Resource<Quote>,
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
        characterInfo.data?.docs?.forEachIndexed { index, character ->
            CharacterNameHeading(data = character) //name header
            CharacterRaceSection(data = character) //race
            CharacterRealmSection(data = character) //realm
            CharacterGenderSection(data = character) //gender
            CharacterDetailDataSection(data = character) //birth and death
            CharacterWikiUrlSection(data = character)
        }
        CharacterQuoteSection(characterQuote = characterQuote)
        /*val quotes = quoteInfo.data?.docs?.map {
            it.dialog.trim()
        }
        CharacterQuoteSection(quoteInfo = quotes)*/
        /*quoteInfo.data?.docs?.forEachIndexed { index, quote ->
            CharacterQuoteSection(quote = quote)
        }*/
    }
}

@Composable
fun CharacterNameHeading(
    data: DocX
) {
    Text(
        text = data.name,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier
            .padding(top = 16.dp)
    )
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
                .background(color = Color(5, 131, 7))
                .background(
                    color = when (data.race) {
                        "Human" -> {
                            Color(96, 155, 255)
                        }
                        "Elf" -> {
                            Color(5, 131, 7)
                        }
                        "Dwarf" -> {
                            Color(255, 215, 0)
                        }
                        "Hobbit" -> {
                            Color(84, 39, 39)
                        }
                        else -> {
                            Color.LightGray
                        }
                    }
                )
                .height(35.dp)
        ) {
            Text(
                text = data.race,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CharacterRealmSection(
    data: DocX
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .clip(CircleShape)
                .background(color = Color(103, 4, 103))
                .height(35.dp)
        ) {
            Text(
                text = data.realm,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CharacterGenderSection(
    data: DocX
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .clip(CircleShape)
                .background(
                    color = if (data.gender == "Male") {
                        Color.Blue
                    } else {
                        Color(193, 102, 163)
                    }
                )
                .height(35.dp)
        ) {
            Text(
                text = data.gender,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
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
                .size(60.dp)
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
                .size(60.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = data.death,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun CharacterQuoteSection(
    characterQuote: Resource<Quote>
) {
    val list = characterQuote.data?.docs?.toList()
    var quoteState by remember { mutableStateOf("") }
    var index by remember { mutableStateOf(0) }

    Spacer(modifier = Modifier.size(10.dp))
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Button(onClick = { index-- },
                modifier = Modifier.padding(8.dp),
                enabled = (index != 0)
            ) {
                Text(
                    text = "<",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            /*Box() {
                AnimatedVisibility(
                    visible = false,
                    enter = fadeIn(
                        animationSpec = tween(durationMillis = duration)
                    ),
                    exit = fadeOut(
                        animationSpec = tween(durationMillis = duration)
                    )) {
                    Text(
                        text = title,
                        style = TextStyle(textDecoration=null)
                    )
                }
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(
                        animationSpec = tween(durationMillis = duration)
                    ),
                    exit = fadeOut(
                        animationSpec = tween(durationMillis = duration)
                    )) {
                    Text(
                        text = title,
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                    )
                }
            }*/

            list?.get(index)?.dialog?.let {
                Text(
                    text = it
                )
            }
            Button(onClick = { index++ },
                modifier = Modifier
                .padding(8.dp),
                /*enabled = (index == list!!.size -1)*/
            ) {
                Text(
                    text = ">",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CharacterWikiUrlSection(
    data: DocX
) {
    Spacer(modifier = Modifier.height(20.dp))
    val annotatedLinkString = buildAnnotatedString {
        val str = "Click this link to see ${data.name}'s LOTR Wiki page"
        val startIndex = str.indexOf("link")
        val endIndex = startIndex + 4
        append(str)
        addStyle(
            style = SpanStyle(textDecoration = TextDecoration.Underline),
            start = startIndex,
            end = endIndex
        )
        addStringAnnotation(
            tag = "URL",
            annotation = data.wikiUrl,
            start = startIndex,
            end = endIndex
        )
    }
    val handler = AndroidUriHandler(LocalContext.current)
    Column(modifier = Modifier
        .fillMaxWidth()) {
        ClickableText(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            text = annotatedLinkString,
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            ),
            onClick = {
                annotatedLinkString
                    .getStringAnnotations("URL", it, it)
                    .firstOrNull()?.let { stringAnnotation ->
                        handler.openUri(stringAnnotation.item)
                    }
            }
        )
    }
}


