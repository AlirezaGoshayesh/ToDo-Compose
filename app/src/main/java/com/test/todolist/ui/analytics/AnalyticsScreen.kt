package com.test.todolist.ui.analytics

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.domain.base.Resource
import com.test.todolist.ui.ErrorBox
import com.test.todolist.ui.Loading
import com.test.todolist.ui.home.*
import com.test.todolist.utils.filterDateAndConvertToListOfPairs

@Composable
fun AnalyticsScreen(
    navController: NavController, viewModel: TasksVM
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            title = {},
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.onBackground,
            elevation = 0.dp,
            modifier = Modifier.padding(8.dp),
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            })
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
            Text(
                text = "How you did it!",
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
                    val data =
                        (entries as Resource.Success).data.filterDateAndConvertToListOfPairs(date = null)
                    val doneCount = data.map { entryPair -> entryPair.second }
                        .filter { toDoEntry -> toDoEntry.isDone }.size
                    val undoneCount = data.size - doneCount
                    TopInfoView(doneCount = doneCount, undoneCount = undoneCount)
                    Divider(modifier = Modifier.padding(vertical = 24.dp))
                    //calculate
                    val mostTotalCategoryEntry = data.groupingBy { pair -> pair.first }.eachCount()
                        .maxByOrNull { intEntry -> intEntry.value }
                    var mostTotalCategory = Pair<ToDoCategory?, Int?>(null, null)
                    if (mostTotalCategoryEntry != null)
                        mostTotalCategory =
                            Pair(mostTotalCategoryEntry.key, mostTotalCategoryEntry.value)
                    val mostDoneCategoryEntry =
                        data.filter { pair -> pair.second.isDone }.groupingBy { pair -> pair.first }
                            .eachCount().maxByOrNull { intEntry -> intEntry.value }
                    var mostDoneCategory = Pair<ToDoCategory?, Int?>(null, null)
                    if (mostDoneCategoryEntry != null)
                        mostDoneCategory =
                            Pair(mostDoneCategoryEntry.key, mostDoneCategoryEntry.value)
                    val mostRemainingCategoryEntry = data.filter { pair -> !pair.second.isDone }
                        .groupingBy { pair -> pair.first }.eachCount()
                        .maxByOrNull { intEntry -> intEntry.value }
                    var mostRemainingCategory = Pair<ToDoCategory?, Int?>(null, null)
                    if (mostRemainingCategoryEntry != null)
                        mostRemainingCategory = Pair(
                            mostRemainingCategoryEntry.key,
                            mostRemainingCategoryEntry.value
                        )
                    //show
                    DetailsView(
                        details = listOf(
                            Triple(
                                "The most repeated:",
                                mostTotalCategory.first,
                                mostTotalCategory.second
                            ),
                            Triple(
                                "The most done:",
                                mostDoneCategory.first,
                                mostDoneCategory.second
                            ),
                            Triple(
                                "The most remaining:",
                                mostRemainingCategory.first,
                                mostRemainingCategory.second
                            ),
                        )
                    )
                }
            }
        }
    })
}

@Composable
fun DetailsView(
    details: List<Triple<String, ToDoCategory?, Int?>>,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Categories".uppercase(),
        modifier = Modifier.padding(8.dp),
        color = Color.Gray,
        fontSize = 14.sp
    )
    Spacer(modifier = Modifier.height(4.dp))
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        items(details.size) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = details[it].first,
                    fontSize = 14.sp,
                    maxLines = 1
                )
                var text = "-"
                if (details[it].second != null)
                    text = "${details[it].second?.name}(${details[it].third})"
                Text(
                    text = text,
                    color = if (details[it].second != null) Color(details[it].second!!.color) else MaterialTheme.colors.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun TopInfoView(doneCount: Int, undoneCount: Int, modifier: Modifier = Modifier) {
    Text(
        text = "Tasks".uppercase(),
        modifier = Modifier.padding(8.dp),
        color = Color.Gray,
        fontSize = 14.sp
    )
    Spacer(modifier = Modifier.height(4.dp))
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        var done by remember {
            mutableStateOf(0)
        }
        var unDone by remember {
            mutableStateOf(0)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val doneCounter = animateIntAsState(
                targetValue = done,
                animationSpec = tween(durationMillis = 700, easing = FastOutLinearInEasing)
            )
            Text(text = "Done")
            Spacer(modifier = Modifier.heightIn(4.dp))
            Text(
                text = doneCounter.value.toString(),
                fontSize = 36.sp,
                color = MaterialTheme.colors.primary
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val undoneCounter = animateIntAsState(
                targetValue = unDone,
                animationSpec = tween(durationMillis = 700, easing = FastOutLinearInEasing)
            )
            Text(text = "Remaining")
            Spacer(modifier = Modifier.heightIn(4.dp))
            Text(
                text = undoneCounter.value.toString(),
                fontSize = 36.sp,
                color = MaterialTheme.colors.primary
            )
        }
        LaunchedEffect(true) {
            done = doneCount
            unDone = undoneCount
        }
    }
}
