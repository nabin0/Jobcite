package com.nabin0.jobcite.presentation.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabin0.jobcite.data.chat.ChatMessageModel
import com.nabin0.jobcite.presentation.home.disscuss.DiscussScreenEvent
import com.nabin0.jobcite.presentation.home.disscuss.DiscussScreenViewModel
import kotlin.random.Random

@Composable
fun DiscussScreen(
    viewModel: DiscussScreenViewModel
) {

    val state = viewModel.state
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = state.chatList.size) {
        if (state.chatList.isNotEmpty()) {
            listState.scrollToItem(state.chatList.size - 1)
        }
    }

    Box(Modifier.fillMaxSize()) {
        TopAppBar(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
            .background(MaterialTheme.colors.primary),
            title = { Text(text = "Global Chat", fontWeight = FontWeight.Bold) })

        LazyColumn(
            modifier = Modifier
                .padding(top = 56.dp, bottom = 56.dp).fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
        ) {
            items(state.chatList) { item ->
                CustomBox(item)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .padding(bottom = 2.dp, start = 8.dp, end = 8.dp, top = 1.dp)
                .align(Alignment.BottomCenter)
        ) {

            TextField(
                modifier = Modifier
                    .weight(1f)
                    .background(color = MaterialTheme.colors.primary),
                value = state.typedMessage,
                onValueChange = { str ->
                    viewModel.onEvent(DiscussScreenEvent.OnMessageTextChange(str))
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onPrimary,
                    fontSize = 20.sp
                ),
                placeholder = {
                    Text(
                        text = "Enter Your Message...", style = TextStyle(
                            color = MaterialTheme.colors.onPrimary.copy(alpha = 0.5f)
                        )
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                )


            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = { viewModel.onEvent(DiscussScreenEvent.SendMessage) }) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = "Send Message",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Composable
fun CustomBox(chatItem: ChatMessageModel) {

    Box(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterStart)
                .wrapContentWidth()
        ) {
            Text(
                text = chatItem.userName.toString(),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = chatItem.text.toString(),
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
        }

        Canvas(modifier = Modifier.matchParentSize()) {
            val startPoint = Offset(0f, size.height)
            val endPoint = Offset(size.width - 10, size.height)
            val controlPoint = Offset(size.width * 1.06f, size.height*0.6f)
            val strokeWidth = 2.dp.toPx()
            val lineColor = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))

            drawLine(
                color = lineColor, start = startPoint, end = endPoint, strokeWidth = strokeWidth
            )

            drawLine(
                color = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)), start = endPoint, end = controlPoint, strokeWidth = strokeWidth
            )

            drawCircle(
                color = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)),
                radius = 2.dp.toPx(),
                center = controlPoint
            )

        }
    }
}
