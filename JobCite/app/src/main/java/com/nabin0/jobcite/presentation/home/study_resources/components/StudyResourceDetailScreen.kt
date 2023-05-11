package com.nabin0.jobcite.presentation.home.study_resources.components

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Money
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nabin0.jobcite.R
import com.nabin0.jobcite.Utils
import com.nabin0.jobcite.data.study_resources.model.StudyResourceModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StudyResourceDetailScreen(dataItem: StudyResourceModel?, navHostController: NavHostController) {

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 2.dp, bottom = 2.dp, start = 5.dp, end = 5.dp)
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        dataItem?.title?.let {
            Text(text = it, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
        }
        Spacer(modifier = Modifier.height(10.dp))
        dataItem?.link?.let {

            SelectionContainer(modifier = Modifier.clickable {
                clipboardManager.setText(AnnotatedString(it))
                Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
            }) {
                Text(buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.onBackground
                        )
                    ) {
                        append("Link: [CLICK TO COPY] ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = colorResource(id = R.color.primary2), fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(it)
                    }
                }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        dataItem?.creator?.let {
            Text(buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.onBackground
                    )
                ) {
                    append("Creator: ")
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold
                    )
                ) {
                    append(it)
                }
            })
        }
        Spacer(modifier = Modifier.height(8.dp))
        dataItem?.language?.let {
            Text(buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.onBackground
                    )
                ) {
                    append("Language: ")
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold
                    )
                ) {
                    append(it)
                }
            })
        }
        Spacer(modifier = Modifier.height(8.dp))
        dataItem?.contentPlatform?.let {
            Text(buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.onBackground
                    )
                ) {
                    append("Content Uploaded on: ")
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold
                    )
                ) {
                    append(it)
                }
            })
        }
        Spacer(modifier = Modifier.height(8.dp))
        dataItem?.free?.let {
            Chip(onClick = { }, shape = MaterialTheme.shapes.medium, leadingIcon = {
                if (it) {
                    Icon(
                        imageVector = Icons.Filled.AttachMoney,
                        contentDescription = "Tag Icon",
                        tint = Color.Green
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.AttachMoney,
                        contentDescription = "Tag Icon",
                        tint = Color.Red
                    )
                }
            }) {
                Text(text = if (dataItem.free == true) "Free" else "Paid")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        dataItem?.submittedDate?.let {
            Text(buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.onBackground
                    )
                ) {
                    append("Upload Date: ")
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold
                    )
                ) {
                    append(Utils.fromatDateFromDateObject(it))
                }
            })
        }
        Spacer(modifier = Modifier.height(10.dp))
        dataItem?.tags?.let { tags ->
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {

                items(tags) { tag ->
                    Chip(
                        onClick = {},
                        shape = MaterialTheme.shapes.small,
                        colors = ChipDefaults.chipColors(
                            contentColor = Color.Black,
                            backgroundColor = colorResource(id = R.color.primary2).copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(text = tag)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        dataItem?.description?.let {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(imageVector = Icons.Filled.FormatQuote,
                    contentDescription = "Format Quote",
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = 180f
                        }
                        .size(25.dp)
                        .fillMaxWidth(0.4f))
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = it, style = TextStyle(
                        fontSize = 16.sp, fontFamily = FontFamily.SansSerif
                    ),
                    modifier = Modifier.fillMaxWidth(0.92f)
                )

                Spacer(modifier = Modifier.height(5.dp))
                Icon(
                    imageVector = Icons.Filled.FormatQuote,
                    contentDescription = "Format Quote",
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.Bottom)
                        .fillMaxWidth(0.4f)
                )
            }
        }
    }
}