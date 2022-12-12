package com.test.todolist.ui.addCategory

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.util.*

@Composable
fun AddCategoryDialog(
    onClick: (String) -> Unit,
    onDismiss: () -> Unit,
    categories: List<String>,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth(), elevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "Create new category",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(16.dp))
                var isValueChanged by remember {
                    mutableStateOf(false)
                }
                var textState by remember { mutableStateOf(TextFieldValue()) }
                val error = if (categories.contains(textState.text.lowercase(Locale.ROOT)))
                    "Name already exists!"
                else {
                    ""
                }
                TextField(
                    value = textState,
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                    onValueChange = { fieldValue ->
                        isValueChanged = true
                        textState = fieldValue
                    },
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth()
                        .padding(4.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    placeholder = { Text(text = "Category name", fontSize = 20.sp, modifier = Modifier.fillMaxWidth()) },
                    singleLine = true,
                    trailingIcon = {
                        if (error.isNotEmpty() && isValueChanged)
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colors.error
                            )
                    }
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    if (error.isEmpty() && textState.text.isNotEmpty())
                        onClick(textState.text)
                }) {
                    Text(text = "Confirm", fontSize = 18.sp)
                }
                Text(
                    text = if (isValueChanged) error else "",
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.error
                )
            }
        }
    }
}