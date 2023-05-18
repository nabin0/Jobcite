package com.nabin0.jobcite.presentation.home.settings

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nabin0.jobcite.Constants
import com.nabin0.jobcite.R
import com.nabin0.jobcite.presentation.screens.AuthScreens
import com.nabin0.jobcite.presentation.screens.Screens

@Composable
fun SettingsScreen(
    navHostController: NavHostController,
    viewModel: SettingsViewModel,
    toggleTheme: () -> Unit,
    rootNavHost: NavHostController
) {
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(),
            onResult = {
            })
    val state = viewModel.state
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
            .background(MaterialTheme.colors.primary),
            title = { Text(text = "Settings", fontWeight = FontWeight.Bold) })

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 86.dp, start = 20.dp, end = 20.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Account Info",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Name",
                            style = TextStyle(fontSize = 20.sp)
                        )
                        Text(
                            text = state.userName.toString(),
                            style = TextStyle(fontSize = 20.sp)
                        )
                    }
                    Divider(
                        thickness = 1.dp,
                        color = MaterialTheme.colors.onPrimary.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Email",
                            style = TextStyle(fontSize = 20.sp)
                        )
                        Text(
                            text = state.userEmail.toString(),
                            style = TextStyle(fontSize = 20.sp)
                        )
                    }
                    Divider(
                        thickness = 1.dp,
                        color = MaterialTheme.colors.onPrimary.copy(alpha = 0.2f)
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
            }

            item {
                Column {
                    Text(
                        text = "Account/App Controls",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .height(60.dp)
                            .clickable {
                                viewModel.onEvent(SettingScreenEvents.SignOut)
                                rootNavHost.navigate(AuthScreens.SignInScreen.route) {
                                    popUpTo(rootNavHost.graph.id) {
                                        inclusive = true
                                    }
                                }
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(0.7f)) {
                            Icon(
                                imageVector = Icons.Outlined.Logout,
                                contentDescription = "logout",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Logout",
                                style = TextStyle(fontSize = 20.sp),
                            )
                        }
                        Icon(
                            imageVector = Icons.Outlined.ChevronRight,
                            contentDescription = "chevron right",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Divider(
                        thickness = 1.dp,
                        color = MaterialTheme.colors.onPrimary.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .height(60.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(0.7f)) {
                            Icon(
                                imageVector = Icons.Outlined.DarkMode,
                                contentDescription = "dark mode",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Dark Mode",
                                style = TextStyle(fontSize = 20.sp)
                            )
                        }
                        Switch(checked = state.isDarkModeOn, onCheckedChange = {
                            toggleTheme()
                            viewModel.onEvent(SettingScreenEvents.OnDarkModeSwitchToggle(it))
                        })
                    }
                }
                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colors.onPrimary.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
                Column {
                    Text(
                        text = "About Application",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clickable {
                                navHostController.navigate(Screens.AboutAppScreen.route)
                            }
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info, contentDescription = "about",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "About",
                                style = TextStyle(fontSize = 20.sp)
                            )
                        }
                        Icon(
                            imageVector = Icons.Outlined.ChevronRight,
                            contentDescription = "chevron right",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Divider(
                        thickness = 1.dp,
                        color = MaterialTheme.colors.onPrimary.copy(alpha = 0.2f)
                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .height(60.dp).clickable {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(Constants.GITHUB_SOURCE_CODE_LINK)
                                )
                                launcher.launch(intent)
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier
                            .fillMaxWidth(0.7f)
                            ) {
                            Icon(
                                imageVector = Icons.Outlined.Code,
                                contentDescription = "source code",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Source Code",
                                style = TextStyle(
                                    fontSize = 20.sp
                                )
                            )
                        }
                        Icon(
                            imageVector = Icons.Outlined.ChevronRight,
                            contentDescription = "log out",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Divider(
                        thickness = 1.dp,
                        color = MaterialTheme.colors.onPrimary.copy(alpha = 0.2f)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Version 1.0",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

            }
        }
    }

}

@Composable
fun AboutApp() {
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Jobcite", style = TextStyle(fontSize = 30.sp, color = colorResource(id = R.color.primary2)))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Version 1.00.000",
            style = TextStyle(fontSize = 14.sp, color = MaterialTheme.colors.onPrimary)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "@ Copyright 2023 Jobcite",
            style = TextStyle(fontSize = 14.sp, color = MaterialTheme.colors.onPrimary)
        )
    }
}