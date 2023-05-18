package com.nabin0.jobcite.presentation.home.events

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabin0.jobcite.Constants.TAG
import com.nabin0.jobcite.data.EventsRepository
import com.nabin0.jobcite.data.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventsRepository: EventsRepository
) : ViewModel() {
    private val _eventsList = mutableStateListOf<String>()
    val eventsList: List<String> = _eventsList


    fun getAllEvents() {
        viewModelScope.launch {
            eventsRepository.getEventsFromFirebase {
                when (it) {
                    is Resource.Failure -> {
                        Log.d(TAG, "getAllEvents: failed")

                    }

                    Resource.Loading -> TODO()
                    is Resource.Success -> {
                        _eventsList.clear()
                        _eventsList.addAll(it.result)
                        Log.d(TAG, "getAllEvents: ${eventsList.size}")
                    }
                }
            }
        }
    }
}

@Composable
fun EventsScreen(
    eventsViewModel: EventsViewModel
) {
    LaunchedEffect(key1 = Unit) {
        eventsViewModel.getAllEvents()
    }
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = eventsViewModel.eventsList.size) {
        if (eventsViewModel.eventsList.isNotEmpty()) {
            listState.scrollToItem(eventsViewModel.eventsList.size - 1)
        }
    }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(),
            onResult = {
            })
    val eventsList = eventsViewModel.eventsList
    val context = LocalContext.current
    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = {
            Text(
                text = "Events",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
        })
        LazyColumn(Modifier.fillMaxSize(), state = listState) {


            items(eventsList) {
                val randomColor = Color(
                    red = Random.nextInt(256),
                    green = Random.nextInt(256),
                    blue = Random.nextInt(256),
                    50
                )
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10))
                        .background(randomColor)
                        .padding(16.dp)

                ) {
                    HighlightedText(text = it) { link ->
                        Toast.makeText(context, link, Toast.LENGTH_SHORT).show()
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        launcher.launch(intent)
                    }
                }
            }
        }
    }
}

@Composable
fun HighlightedText(
    text: String,
    onClick: (String) -> Unit = {}
) {
    val pattern = Regex("\\*\\*(.*?)\\*\\*")
    val matches = pattern.findAll(text)
    val annotatedStringBuilder = AnnotatedString.Builder()


    var currentIndex = 0
    for (match in matches) {
        var (matchedText) = match.groupValues.drop(0)
        if (matchedText.startsWith("**")) matchedText = matchedText.removePrefix("**")
        if (matchedText.endsWith("**")) matchedText = matchedText.removeSuffix("**")
        val startIndex = match.range.first
        val endIndex = match.range.last + 1

        annotatedStringBuilder.withStyle(
            style = SpanStyle(
                color = MaterialTheme.colors.onPrimary,
                fontSize = 18.sp
            )
        ) {
            append(text.substring(currentIndex, startIndex))
        }

        annotatedStringBuilder.withStyle(style = SpanStyle(color = Color.Blue, fontSize = 14.sp)) {
            pushStringAnnotation(tag = matchedText, annotation = matchedText)
            append(matchedText)
        }

        currentIndex = endIndex
    }

    if (currentIndex < text.length) {
        annotatedStringBuilder.withStyle(
            style = SpanStyle(
                color = MaterialTheme.colors.onPrimary,
                fontSize = 18.sp
            )
        ) {
            append(text.substring(currentIndex))
        }
    }

    val annotatedString = annotatedStringBuilder.toAnnotatedString()

    ClickableText(text = annotatedString, onClick = { offset ->
        annotatedString.getStringAnnotations(offset, offset)
            .firstOrNull()?.let { span ->
                onClick(span.item)
            }
    })
}
