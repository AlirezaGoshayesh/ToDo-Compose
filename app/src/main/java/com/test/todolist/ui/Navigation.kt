package com.test.todolist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.test.todolist.ui.addTask.AddScreen
import com.test.todolist.ui.analytics.AnalyticsScreen
import com.test.todolist.ui.home.HomeScreen
import com.test.todolist.ui.home.TasksVM

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            val entry = remember(it) {
                navController.getBackStackEntry(Screen.HomeScreen.route)
            }
            val tasksVM = hiltViewModel<TasksVM>(entry)
            HomeScreen(navController = navController, tasksVM)
        }
        composable(
            route = Screen.AddScreen.route
        ) {
            val entry = remember(it) {
                navController.getBackStackEntry(Screen.HomeScreen.route)
            }
            val tasksVM = hiltViewModel<TasksVM>(entry)
            AddScreen(navController = navController, tasksVM)
        }
        composable(
            route = Screen.AnalyticsScreen.route
        ) {
            val entry = remember(it) {
                navController.getBackStackEntry(Screen.HomeScreen.route)
            }
            val tasksVM = hiltViewModel<TasksVM>(entry)
            AnalyticsScreen(navController = navController, tasksVM)
        }
    }
}