package com.nabin0.jobcite.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun ResourcesScreenAppBarWithSearchWidget(
    title: String?,
    searchTextValue: String,
    isSearchBarVisible: Boolean,
    onSearchIconClick: () -> Unit,
    onCloseIconClick: () -> Unit,
    onSearchValueChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    if (isSearchBarVisible) {
        SearchBar(
            searchTextValue = searchTextValue,
            onSearchValueChange = onSearchValueChange,
            onCloseIconClick = {
                onCloseIconClick()
            },
            onSearch = onSearch
        )
    } else {
        DefaultToolbar(title = title, onSearchIconClicked = { onSearchIconClick() })
    }
}

@Composable
fun DefaultToolbar(title: String?, onSearchIconClicked: () -> Unit) {
    TopAppBar(title = {
        Text(text = title.toString(), color = MaterialTheme.colors.onPrimary, fontWeight = FontWeight.Bold)
    }, actions = {
        IconButton(onClick = { onSearchIconClicked() }) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colors.onPrimary
            )
        }
    }, modifier = Modifier
        .padding(bottom = 5.dp)
        .height(56.dp)
    )
}

@Composable
fun SearchBar(
    searchTextValue: String,
    onSearchValueChange: (String) -> Unit,
    onCloseIconClick: () -> Unit,
    onSearch: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(bottom = 5.dp)
            .background(color = MaterialTheme.colors.primary)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.primary),
            value = searchTextValue,
            onValueChange = { str ->
                onSearchValueChange(str)
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.onPrimary
            ),
            leadingIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = { onCloseIconClick() }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close Icon",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            },
            placeholder = {
                Text(
                    text = "Search...", style = TextStyle(
                        color = MaterialTheme.colors.onPrimary.copy(alpha = 0.6f)
                    )
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearch()
            })
        )
    }
}