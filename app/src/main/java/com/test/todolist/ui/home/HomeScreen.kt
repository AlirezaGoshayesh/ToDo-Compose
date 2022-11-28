package com.test.todolist.ui.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry
import com.test.todolist.domain.base.Resource
import com.test.todolist.ui.Screen
import com.test.todolist.ui.addCategory.AddCategoryDialog
import com.test.todolist.utils.filterTodayAndConvertToPairs
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun HomeScreen(
    navController: NavController, viewModel: TasksVM
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val canAddTask = remember {
        mutableStateOf(false)
    }
    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "What's my task!?",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.onBackground,
            elevation = 0.dp
        )
    }, floatingActionButtonPosition = FabPosition.End, floatingActionButton = {
        FloatingActionButton(onClick = {
            if (canAddTask.value)
                navController.navigate(Screen.AddScreen.route)
            else
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "First, add a category!"
                    )
                }
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "fab icon")
        }
    }, content = {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(12.dp)
                .background(MaterialTheme.colors.background)
        ) {
            val entries by viewModel.toDoEntries
            when (entries) {
                is Resource.Error -> ErrorBox(
                    text = (entries as Resource.Error).errorModel.getErrorMessage()
                )
                is Resource.Loading -> Loading()
                is Resource.Success -> {
                    var openAddCategoryDialog by remember { mutableStateOf(false) }
                    CategoriesSection(
                        allData = (entries as Resource.Success).data,
                        canAddTask,
                        onClick = {
                            openAddCategoryDialog = true
                        })
                    if (openAddCategoryDialog)
                        AddCategoryDialog(
                            onClick = { name ->
                                viewModel.addToDoCategory(name = name)
                                openAddCategoryDialog = false
                            },
                            onDismiss = { openAddCategoryDialog = false },
                            categories = (entries as Resource.Success).data.keys.map { toDoCategory ->
                                toDoCategory.name.lowercase(
                                    Locale.ROOT
                                )
                            }
                        )
                    Spacer(modifier = Modifier.height(12.dp))
                    TodayToDoList(
                        todayData = (entries as Resource.Success).data.filterTodayAndConvertToPairs(),
                        onClick = { toDoEntry ->
                            viewModel.editToDoEntry(toDoEntry)
                        })
                }
            }
        }
    })
}

@Composable
fun CategoriesSection(
    allData: Map<ToDoCategory, List<ToDoEntry>>,
    canAddTask: MutableState<Boolean>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = ("categories").uppercase(),
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        val categories = allData.keys.toList()
        canAddTask.value = categories.isNotEmpty()
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            contentPadding = PaddingValues(4.dp)
        ) {
            items(categories.size) {
                val category = categories[it]
                CategorySection(
                    category,
                    allData[category]!!,
                    modifier = Modifier.size(width = 200.dp, height = 140.dp)
                )
            }
            item {
                AddCategorySection(
                    onClick = onClick,
                    modifier = Modifier.size(140.dp)
                )
            }
        }
    }

}

@Composable
fun AddCategorySection(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick.invoke() }, elevation = 2.dp, shape = RoundedCornerShape(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.padding(8.dp),
            tint = MaterialTheme.colors.primary
        )
    }
}

@Composable
fun CategorySection(category: ToDoCategory, tasks: List<ToDoEntry>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp), elevation = 2.dp, shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "${tasks.size} tasks",
                color = Color.Gray,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = category.name,
                color = MaterialTheme.colors.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            var indicatorProgress = tasks.filter { it.isDone }.size.toFloat() / tasks.size
            if (!indicatorProgress.isNaN())
                indicatorProgress = 0f
            val progressAnimation by animateFloatAsState(
                targetValue = indicatorProgress,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .height(8.dp),
                progress = progressAnimation,
                backgroundColor = Color.LightGray,
                color = Color(category.color)
            )
        }
    }
}

@Composable
fun ErrorBox(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.error,
    textColor: Color = MaterialTheme.colors.onError
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp), shape = CircleShape, color = backgroundColor
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            textAlign = TextAlign.Center,
            color = textColor
        )
    }
}

@Composable
fun Loading(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier.size(48.dp))
}

@Composable
fun TodayToDoList(
    todayData: List<Pair<ToDoCategory, ToDoEntry>>,
    onClick: (todo: ToDoEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = ("today's tasks").uppercase(),
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
    if (todayData.isEmpty()) ErrorBox(
        text = "No Entries Found",
        backgroundColor = Color.Transparent,
        textColor = MaterialTheme.colors.primary
    )
    else LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(todayData.size) {
            ToDoSection(
                todo = todayData[it],
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ToDoSection(
    todo: Pair<ToDoCategory, ToDoEntry>,
    onClick: (todo: ToDoEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    val isCheck = todo.second.isDone
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .padding(12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(if (isCheck) Color.LightGray else Color.Transparent, CircleShape)
                    .border(
                        if (isCheck) BorderStroke(
                            0.dp,
                            Color.Transparent
                        ) else BorderStroke(2.dp, Color(todo.first.color)), CircleShape
                    )
                    .clickable {
                        onClick(todo.second)
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                if (isCheck)
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                fontSize = 16.sp,
                text = todo.second.title,
                textDecoration = if (isCheck) TextDecoration.LineThrough else TextDecoration.None
            )
        }
    }
}

@Composable
fun ToDo(
    todo: ToDoEntry, modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(4.dp)
                .weight(1f)
        ) {
            Text(
                text = todo.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = todo.desc, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}
