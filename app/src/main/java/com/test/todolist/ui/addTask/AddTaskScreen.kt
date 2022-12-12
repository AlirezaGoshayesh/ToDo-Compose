package com.test.todolist.ui.addTask

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.domain.base.Resource
import com.test.todolist.ui.ErrorBox
import com.test.todolist.ui.Loading
import com.test.todolist.ui.home.TasksVM
import com.test.todolist.utils.DateUtils.makeDatePicker
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddScreen(
    navController: NavController, viewModel: TasksVM
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
        viewModel.getCategories()
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
                .background(MaterialTheme.colors.background)
        ) {
            val categories by viewModel.toDoCategories.collectAsState()
            when (categories) {
                is Resource.Error -> ErrorBox(
                    text = (categories as Resource.Error).errorModel.getErrorMessage()
                )
                is Resource.Loading -> Loading()
                is Resource.Success -> {
                    val localFocusManager = LocalFocusManager.current
                    var openCategoriesDialog by remember { mutableStateOf(false) }
                    val selectedDate = remember {
                        mutableStateOf(Date())
                    }
                    var selectedCategory by remember {
                        mutableStateOf((categories as Resource.Success<List<ToDoCategory>>).data.first())
                    }
                    val mDatePickerDialog =
                        makeDatePicker(selectedDate, LocalContext.current, isMinDateToday = true)
                    val focusRequester = remember { FocusRequester() }
                    var textState by remember { mutableStateOf(TextFieldValue()) }
                    TextField(
                        value = textState,
                        textStyle = TextStyle.Default.copy(fontSize = 24.sp),
                        onValueChange = { fieldValue -> textState = fieldValue },
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            localFocusManager.moveFocus(FocusDirection.Down)
                        }),
                        placeholder = { Text(text = "Enter new task", fontSize = 24.sp) },
                        singleLine = true
                    )
                    var descriptionState by remember { mutableStateOf(TextFieldValue()) }
                    val scroll = rememberScrollState(0)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .border(
                                BorderStroke(1.dp, Color.LightGray),
                                shape = MaterialTheme.shapes.medium
                            )
                    ) {
                        TextField(
                            value = descriptionState,
                            textStyle = TextStyle.Default.copy(fontSize = 14.sp),
                            onValueChange = { fieldValue -> descriptionState = fieldValue },
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scroll),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(onNext = {
                                localFocusManager.clearFocus()
                            }),
                            placeholder = {
                                Text(
                                    text = "Description(optional)",
                                    fontSize = 14.sp
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                    }
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val localDensity = LocalDensity.current
                        var heightIs by remember {
                            mutableStateOf(0.dp)
                        }
                        Box(
                            modifier = Modifier
                                .clickable {
                                    mDatePickerDialog.show()
                                }
                                .onGloballyPositioned { coordinates ->
                                    heightIs = with(localDensity) { coordinates.size.height.toDp() }
                                }
                                .border(0.5.dp, Color.LightGray, RoundedCornerShape(24.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .align(Alignment.Center)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
                                Text(
                                    text = formatter.format(selectedDate.value),
                                    color = Color.Gray
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clickable {
                                    openCategoriesDialog = true
                                }
                                .size(heightIs)
                                .border(0.5.dp, Color.LightGray, CircleShape)
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxSize(0.4f)
                                    .border(1.dp, Color(selectedCategory.color), CircleShape)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(0.6f)
                                        .align(Alignment.Center)
                                        .clip(
                                            CircleShape
                                        )
                                        .background(Color(selectedCategory.color))
                                )
                            }
                        }
                    }
                    Button(
                        onClick = {
                            viewModel.addToDoEntry(
                                selectedCategory.id,
                                textState.text,
                                descriptionState.text,
                                selectedDate.value
                            )
                            navController.navigateUp()
                        },
                        shape = RoundedCornerShape(32.dp),
                        enabled = textState.text.isNotEmpty(),
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (textState.text.isNotEmpty()) MaterialTheme.colors.primary else Color.DarkGray
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(text = "New task")
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = null,
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                    if (openCategoriesDialog)
                        CategoriesDialog(
                            onClick = { category ->
                                selectedCategory = category
                                openCategoriesDialog = false
                            },
                            onDismiss = { openCategoriesDialog = false },
                            categories = (categories as Resource.Success<List<ToDoCategory>>).data
                        )
                }
            }
        }
    })

}