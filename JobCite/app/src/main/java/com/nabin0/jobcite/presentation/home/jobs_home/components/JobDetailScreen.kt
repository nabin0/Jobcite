package com.nabin0.jobcite.presentation.home.jobs_home.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nabin0.jobcite.data.jobs.model.JobsModelItem
import com.nabin0.jobcite.presentation.home.jobs_home.JobDetailUiEvents
import com.nabin0.jobcite.presentation.home.jobs_home.JobsDetailViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun JobDetailScreen(
    dataItem: JobsModelItem?,
    navHostController: NavHostController,
    jobsDetailViewModel: JobsDetailViewModel
) {

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navBackStackEntry = navHostController.previousBackStackEntry ?: return
                navBackStackEntry.arguments?.remove("dataItem")
                navHostController.popBackStack()
            }

        }
    }

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val lifecycle = LocalLifecycleOwner.current
    DisposableEffect(onBackPressedDispatcher, lifecycle) {
        onBackPressedDispatcher?.addCallback(lifecycle, backCallback)
        onDispose {
            backCallback.remove()
        }
    }


    LaunchedEffect(key1 = Unit) {
        dataItem?.let {
            jobsDetailViewModel.hasJobItem(jobItem = it)
        }
    }

    val bookmarked = jobsDetailViewModel.isBookmarked.value

    val intentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {})

    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        jobsDetailViewModel.uiEventChannel.collect() { event ->
            when (event) {
                is JobDetailUiEvents.Failure -> {
                    Toast.makeText(context, event.failureMsg, Toast.LENGTH_SHORT).show()
                }

                is JobDetailUiEvents.Success -> {
                    Toast.makeText(context, event.successMsg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(6.dp)
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        dataItem?.jobTitle?.let {
            Text(text = it, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
        }
        dataItem?.companyName?.let {
            Text(
                text = it, style = TextStyle(
                    fontSize = 16.sp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        dataItem?.location?.let {
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Location icon",
                    modifier = Modifier
                        .size(26.dp)
                        .padding(end = 5.dp)
                )
                Text(
                    text = it, style = TextStyle(
                        fontSize = 16.sp, color = MaterialTheme.colors.onSurface
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        dataItem?.salary?.let {
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Outlined.AccountBalanceWallet,
                    contentDescription = "Salary icon",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 5.dp)
                )
                Text(
                    text = it, style = TextStyle(
                        fontSize = 16.sp, color = MaterialTheme.colors.onSurface
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        dataItem?.jobPostedOn?.let {
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Outlined.Update,
                    contentDescription = "recent icon",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 5.dp)
                )
                Text(
                    text = it, style = TextStyle(
                        fontSize = 16.sp, color = MaterialTheme.colors.onSurface
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            dataItem?.experienceRequired?.let {
                Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                    Icon(
                        imageVector = Icons.Outlined.Work,
                        contentDescription = "work icon",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 5.dp)
                    )
                    Text(
                        text = it, style = TextStyle(
                            fontSize = 16.sp, color = MaterialTheme.colors.onSurface
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            val bookmarkIcon =
                if (bookmarked) Icons.Default.BookmarkAdded else Icons.Default.BookmarkAdd
            Icon(imageVector = bookmarkIcon,
                contentDescription = "Bookmark icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        dataItem?.let { item ->
                            jobsDetailViewModel.saveJobItem(item)
                            jobsDetailViewModel.isBookmarked.value = true
                        }
                    }.padding(end = 5.dp))
        }


        Divider(modifier = Modifier.padding(8.dp), color = Color.Gray, thickness = 2.dp)

        Text(
            text = "About Job", modifier = Modifier.fillMaxWidth(), style = TextStyle(
                fontSize = 25.sp, fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(10.dp))

        dataItem?.jobDescription?.let {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(imageVector = Icons.Filled.FormatQuote,
                    contentDescription = "Format Quote",
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = 180f
                        }
                        .size(24.dp)
                        .fillMaxWidth(0.4f))
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = it, style = TextStyle(
                        fontSize = 16.sp, fontFamily = FontFamily.SansSerif
                    ), modifier = Modifier.fillMaxWidth(0.92f)
                )

                Spacer(modifier = Modifier.height(5.dp))
                Icon(
                    imageVector = Icons.Filled.FormatQuote,
                    contentDescription = "Format Quote",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Bottom)
                        .fillMaxWidth(0.4f)
                )

            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        dataItem?.jobSkills?.let { tags ->
            Text(
                text = "Skills required",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                fontSize = 20.sp
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(tags) { tag ->
                    Chip(
                        onClick = {},
                        shape = MaterialTheme.shapes.small,
                        colors = ChipDefaults.chipColors(
                            contentColor = Color.Black,
                            backgroundColor = Color(0XFF30E3DF).copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(text = tag)
                    }
                }
            }
        }

        dataItem?.jobBasicInfo?.let { jobInfo ->
            Text(
                text = "Other Info", style = TextStyle(
                    fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            jobInfo.industry?.let {
                Text(text = "Industry: $it", fontSize = 16.sp, modifier = Modifier.padding(4.dp))
                Spacer(modifier = Modifier.height(5.dp))
            }
            jobInfo.jobFunction?.let {
                Text(
                    text = "Job Function: $it", fontSize = 16.sp, modifier = Modifier.padding(4.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            jobInfo.qualification?.let {
                Text(
                    text = "Qualification: $it", fontSize = 16.sp, modifier = Modifier.padding(4.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            jobInfo.specialization?.let {
                Text(
                    text = "Specialization: $it",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(4.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            dataItem?.jobPostLink?.let {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                        intentLauncher.launch(intent)
                    }, modifier = Modifier
                        .height(45.dp)
                        .background(Color(0XFF30E3DF))
                        .weight(1f)
                ) {
                    Text(text = "Job Post", color = Color(0XFF30E3DF), fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.width(5.dp))
            dataItem?.hiringCompanyLink?.let {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                        intentLauncher.launch(intent)
                    }, modifier = Modifier
                        .height(45.dp)
                        .background(Color(0XFF30E3DF))
                        .weight(1f)
                ) {
                    Text(
                        text = "Company Website",
                        color = Color(0XFF30E3DF),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}