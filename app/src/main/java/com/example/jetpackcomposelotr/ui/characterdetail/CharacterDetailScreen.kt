package com.example.jetpackcomposelotr.ui.characterdetail

import kotlin.concurrent.schedule
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Forward
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.AndroidUriHandler
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
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
import com.example.jetpackcomposelotr.data.remote.responses.DocXX
import com.example.jetpackcomposelotr.data.remote.responses.Quote
import com.example.jetpackcomposelotr.util.Resource
import timber.log.Timber
import java.util.*

@Composable
fun CharacterDetailScreen(
    navController: NavController,
    characterId: String,
    topPadding: Dp = 20.dp,
    characterImageSize: Dp = 200.dp,
    viewModel: CharacterDetailViewModel = hiltViewModel()
) {
    /* val allCharactersFromRoom = viewModel.getAllCharactersFromRoom()
     */

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
            .padding(bottom = 8.dp) //was 16.dp
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
                    bottom = 8.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(color = MaterialTheme.colors.surface)
                .padding(8.dp)
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
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
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
    val quotes = mutableListOf<String>()
    characterQuote.data?.docs?.forEach {
        quotes.add(it.dialog)
    }
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
            CharacterQuoteSection(quotes = quotes) //quote
            if (!character.wikiUrl.isNullOrEmpty()) {
                CharacterWikiUrlSection(data = character) //wiki link
            }
        }
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
            .padding(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .clip(CircleShape)
                .height(35.dp)
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
    // NEED TO FORMAT REALM TEXT TO SPACE AFTER COMMAS, SAME WITH BIRTH/DEATH
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
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
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
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
        Spacer(
            modifier = Modifier
                .size(1.dp, sectionHeight)
                .background(Color.LightGray)
        )
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
            color = MaterialTheme.colors.onSurface,
            fontSize = 22.sp,
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
            color = MaterialTheme.colors.onSurface,
            fontSize = 22.sp,
        )
    }
}

@Composable
fun CharacterWikiUrlSection(
    data: DocX
) {
//    Spacer(modifier = Modifier.height(20.dp))
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        ClickableText(
            modifier = Modifier
//                .padding(6.dp)
                .align(Alignment.CenterHorizontally),
            text = annotatedLinkString,
            style = TextStyle(
                fontSize = 16.sp,
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CharacterQuoteSection(
    quotes: MutableList<String>
) {
    var index by remember { mutableStateOf(0) }
    var visible by remember { mutableStateOf(true) }

    Spacer(modifier = Modifier.size(10.dp))
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(164.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(0.5f)
                .align(Alignment.CenterVertically)
        ) {
            IconButton(
                onClick = {
                    visible = !visible
                    Timer().schedule(1000) {
                        index--
                        visible = !visible
                    }
                },
                modifier = Modifier
                    .padding(4.dp),
                enabled = (index != 0),
                content = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .size(26.dp)
                    )
                }
            )
        }
        Column(
            modifier = Modifier
                .weight(2f)
                .align(Alignment.CenterVertically)
                .fillMaxHeight()
        ) {
            if (!quotes.isNullOrEmpty()) {
                quotes[index].let {
                    val myId = "inlineContent"
                    val text = buildAnnotatedString {
                        appendInlineContent(myId, "[startQuote]")
                        append(it)
                        appendInlineContent(myId, "[endQuote]")
                    }
                    val inlineContent = mapOf(
                        Pair(
                            myId,
                            InlineTextContent(
                                Placeholder(
                                    width = 26.sp,
                                    height = 26.sp,
                                    placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline
                                )
                            ) {
                                Icon(
                                    Icons.Filled.FormatQuote, "",
                                )
                            }
                        )
                    )
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(
                            animationSpec = tween(
                                1000, 0, LinearOutSlowInEasing
                            )
                        ),
                        exit = fadeOut(),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                        ) {
                            Text(
                                color = MaterialTheme.colors.onSecondary,
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .align(Alignment.Center),
                                text = text,
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                fontStyle = FontStyle.Italic,
                                inlineContent = inlineContent
                            )
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(0.5f)
                .align(Alignment.CenterVertically),
            verticalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = {
                    visible = !visible
                    Timer().schedule(700) {
                        visible = if (index >= quotes.size -1) {
                            //no index, so show previous quote
                            !visible
                        } else {
                            index++
                            !visible
                        }
                    }
                },
                enabled = (index != quotes.size -1),
                modifier = Modifier
                    .padding(4.dp),
                content = {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier
                            .size(26.dp)
                    )
                }
            )
        }
    }
}
