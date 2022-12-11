package com.test.todolist.ui.home

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import com.test.todolist.ui.MenuItem
import com.test.todolist.ui.Screen
import com.test.todolist.ui.addCategory.AddCategoryDialog
import com.test.todolist.ui.navigationDrawer.DrawerBody
import com.test.todolist.ui.navigationDrawer.DrawerHeader
import com.test.todolist.ui.theme.Blue900
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
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.DragHandle,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.onBackground,
                elevation = 0.dp
            )
        },
        drawerContent = {
            DrawerHeader("Your Name", onBackClick = {
                coroutineScope.launch {
                    scaffoldState.drawerState.close()
                }
            })
            DrawerBody(
                items = listOf(
                    MenuItem(
                        id = "analytics",
                        title = "Analytics",
                        icon = Icons.Outlined.Analytics,
                        contentDescription = "Analytics"
                    )
                ), onItemClick = { menuItem ->
                }
            )
        },
        drawerBackgroundColor = Blue900,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                onClick = {
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
        },
        content = {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(12.dp)
                    .background(MaterialTheme.colors.background)
            ) {
                Text(
                    text = "What's my task!?",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(36.dp))
                val entries by viewModel.toDoEntries.collectAsState()
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
                        Spacer(modifier = Modifier.height(24.dp))
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
            .clickable { onClick.invoke() }, elevation = 0.dp, shape = RoundedCornerShape(16.dp)
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
            .padding(8.dp), elevation = 0.dp, shape = RoundedCornerShape(16.dp)
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
            var progress by remember { mutableStateOf(0f) }
            var indicatorProgress = tasks.filter { it.isDone }.size.toFloat() / tasks.size
            if (indicatorProgress.isNaN())
                indicatorProgress = 0f
            val progressAnimation by animateFloatAsState(
                targetValue = progress,
                animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
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
            LaunchedEffect(true) {
                progress = indicatorProgress
            }
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
    Box(modifier = modifier.fillMaxWidth()) {
        CircularProgressIndicator(
            modifier
                .size(64.dp)
                .align(Alignment.Center)
                .padding(8.dp)
        )
    }
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
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
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
        elevation = 0.dp
    ) {
        var isExpanded by remember {
            mutableStateOf(false)
        }
        val rotateState by animateFloatAsState(
            targetValue = if (isExpanded) 180F else 0F,
        )
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            if (isCheck) Color.LightGray else Color.Transparent,
                            CircleShape
                        )
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
                if (todo.second.desc.isNotEmpty()) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        modifier = Modifier
                            .clickable { isExpanded = !isExpanded }
                            .rotate(rotateState),
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
            AnimatedVisibility(visible = isExpanded) {
                Text(
                    text = todo.second.desc,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    }
}