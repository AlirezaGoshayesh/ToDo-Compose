package com.test.todolist.ui.navigationDrawer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.todolist.ui.MenuItem

@Composable
fun DrawerHeader(
    name: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(32.dp)
            .fillMaxWidth()
    ) {
        Button(
            modifier = Modifier
                .align(Alignment.End)
                .size(48.dp),
            onClick = onBackClick,
            shape = CircleShape,
            border = BorderStroke(1.dp, Color.LightGray),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Color.LightGray
            )
        }
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = name,
            fontSize = 24.sp,
            maxLines = 1,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun DrawerBody(
    items: List<MenuItem>,
    onItemClick: (MenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(32.dp), horizontalAlignment = Alignment.Start) {
        items(items.size) {
            Row(
                modifier = Modifier.clickable { onItemClick(items[it]) },
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = items[it].icon,
                    contentDescription = items[it].contentDescription,
                    tint = Color.LightGray
                )
                Text(text = items[it].title, color = Color.LightGray)
            }
        }
    }
}