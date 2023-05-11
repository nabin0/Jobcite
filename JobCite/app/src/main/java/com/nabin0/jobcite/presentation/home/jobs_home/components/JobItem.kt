package com.nabin0.jobcite.presentation.home.jobs_home

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Update
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nabin0.jobcite.Constants
import com.nabin0.jobcite.R
import com.nabin0.jobcite.data.jobs.model.JobsModelItem
import com.nabin0.jobcite.presentation.screens.Screens

@Composable
fun JobItem(
    modifier: Modifier,
    dataItem: JobsModelItem,
    intentLauncher: ActivityResultLauncher<Intent>,
    navController: NavHostController,
    showDeleteIcon: Boolean = false,
    onDeleteClick: (() -> Unit )? = null
) {
    Card(
        modifier = modifier.padding(2.dp),
        elevation = 1.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(modifier = modifier.padding(6.dp)) {
            dataItem.jobTitle?.let {
                Text(
                    text = it, style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onSurface
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            dataItem.companyName?.let {
                Text(
                    text = it, style = TextStyle(
                        fontSize = 16.sp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            dataItem.location?.let {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Location icon",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 5.dp)
                    )
                    Text(
                        text = it, style = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            dataItem.salary?.let {
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
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            dataItem.jobPostedOn?.let {
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
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Divider(color = Color.Gray, thickness = 2.dp, modifier = Modifier.padding(5.dp))
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showDeleteIcon) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete Icon",
                            modifier = Modifier
                                .size(24.dp).clickable {
                                    onDeleteClick?.invoke()
                                }
                        )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "View Details", style = TextStyle(
                    fontSize = 16.sp, color = colorResource(id = R.color.primary2)
                ), modifier = Modifier
                    .padding(5.dp)
                    .clickable {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            Constants.JOB_DATA_ITEM, dataItem
                        )
                        navController.navigate(Screens.JobDetailScreen.route)
                    })
                Spacer(modifier = Modifier.width(5.dp))
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataItem.jobPostLink))
                        intentLauncher.launch(intent)
                    },
                    modifier = Modifier
                        .height(45.dp)
                        .padding(top = 1.dp, bottom = 1.dp)
                ) {
                    Text(
                        text = "Open in browser", color = colorResource(id = R.color.primary2)
                    )
                }
            }
        }
    }
}