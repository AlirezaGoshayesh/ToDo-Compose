package com.test.todolist.ui.add

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AddScreen(
    navController: NavController, viewModel: AddVM
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            title = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier
                            .border(1.dp, Color.Gray, CircleShape)
                            .size(36.dp)
                            .padding(8.dp)
                            .align(Alignment.CenterEnd)
                            .clickable {
                                navController.navigateUp()
                            }
                    )
                }
            },
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.onBackground,
            elevation = 0.dp,
            modifier = Modifier.padding(8.dp)
        )
    }, content = {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
                .background(MaterialTheme.colors.background)
        ) {
            var textState by remember { mutableStateOf(TextFieldValue()) }
            TextField(
                value = textState,
                textStyle = TextStyle.Default.copy(fontSize = 24.sp),
                onValueChange = { fieldValue -> textState = fieldValue },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                placeholder = { Text(text = "Enter new task", fontSize = 24.sp) },
                singleLine = true
            )
        }
    })

}