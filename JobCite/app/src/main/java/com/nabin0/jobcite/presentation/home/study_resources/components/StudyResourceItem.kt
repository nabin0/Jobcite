package com.nabin0.jobcite.presentation.home.components

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nabin0.jobcite.Constants
import com.nabin0.jobcite.R
import com.nabin0.jobcite.Utils
import com.nabin0.jobcite.data.study_resources.model.StudyResourceModel
import com.nabin0.jobcite.presentation.screens.Screens

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StudyResourceItem(
    modifier: Modifier,
    dataItem: StudyResourceModel,
    intentLauncher: ActivityResultLauncher<Intent>,
    navController: NavHostController
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(5.dp)
            .clickable {
                isExpanded = !isExpanded
            }, elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            if (!isExpanded) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    dataItem.title?.let {
                        Text(
                            text = it,
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .fillMaxWidth(0.9f),
                            style = TextStyle(
                                color = MaterialTheme.colors.onBackground,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 5
                        )
                    }
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Expand/Collapse",
                        modifier = Modifier
                            .size(24.dp)
                            .fillMaxWidth(0.1f)
                            .rotate(if (isExpanded) 180f else 0f)
                            .align(Alignment.Top),
                        tint = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                dataItem.free?.let { free ->
                    if (free) {
                        Chip(onClick = { },
                            shape = MaterialTheme.shapes.medium,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.AttachMoney,
                                    contentDescription = "Tag Icon",
                                    tint = Color.Green
                                )
                            }) {
                            Text(text = "Free")
                        }
                    } else {
                        Chip(onClick = { }, shape = MaterialTheme.shapes.medium, leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.AttachMoney,
                                contentDescription = "Tag Icon",
                                tint = Color.Red
                            )

                        }) {
                            Text(text = "Paid")
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dataItem.title.toString(), modifier = Modifier.fillMaxWidth(0.9f),
                        style = TextStyle(
                            color = MaterialTheme.colors.onBackground,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 2,
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Expand/Collapse",
                        modifier = Modifier
                            .size(24.dp)
                            .fillMaxWidth(0.1f)
                            .rotate(if (isExpanded) 180f else 0f),
                        tint = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = dataItem.creator.toString(), modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = dataItem.contentPlatform.toString(), modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Chip(onClick = { /*TODO*/ }, shape = MaterialTheme.shapes.medium, leadingIcon = {
                    if (dataItem.free == true) {
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
                Spacer(modifier = Modifier.height(5.dp))
                if (dataItem.tags != null) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(dataItem.tags) { tag ->
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
                Spacer(modifier = Modifier.height(5.dp))
                dataItem.submittedDate?.let{
                    Text(buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colors.onBackground
                            )
                        ) {
                            append("Submitted on: ")
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
                Divider(color = Color.Gray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Icon(imageVector = Icons.Outlined.OpenInBrowser,
                        contentDescription = "Open Link",
                        tint = colorResource(id = R.color.primary2),
                        modifier = Modifier
                            .fillMaxWidth(0.33f)
                            .weight(1f)
                            .height(25.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataItem.link))
                                val chooserIntent =
                                    Intent.createChooser(intent, "Choose browser to open the link.")
                                intentLauncher.launch(chooserIntent)
                            })
                    Icon(imageVector = Icons.Rounded.Share,
                        contentDescription = "Share Link",
                        tint = colorResource(id = R.color.primary2),
                        modifier = Modifier
                            .fillMaxWidth(0.33f)
                            .height(25.dp)
                            .weight(1f)
                            .clickable {
                                val intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, dataItem.link)
                                    type = "text/plain"
                                }
                                val chooserIntent =
                                    Intent.createChooser(intent, "Choose app to share.")
                                intentLauncher.launch(chooserIntent)
                            })
                    Icon(imageVector = Icons.Outlined.Info,
                        contentDescription = "Detail",
                        tint = colorResource(id = R.color.primary2),
                        modifier = Modifier
                            .fillMaxWidth(0.33f)
                            .height(25.dp)
                            .weight(1f)
                            .clickable {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    key = Constants.STUDY_RESOURCE_ITEM, value = dataItem
                                )
                                navController.navigate(Screens.StudyResourceDetailScreen.route)
                            })
                }
            }
        }
    }
}
