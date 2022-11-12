package com.test.todolist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.test.todolist.ui.add.AddScreen
import com.test.todolist.ui.add.AddVM
import com.test.todolist.ui.home.HomeScreen
import com.test.todolist.ui.home.HomeVM

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            val entry = remember(it) {
                navController.getBackStackEntry(Screen.HomeScreen.route)
            }
            val homeVM = hiltViewModel<HomeVM>(entry)
            HomeScreen(navController = navController, homeVM)
        }
        composable(
            route = Screen.AddScreen.route) {
            val entry = remember(it) {
                navController.getBackStackEntry(Screen.AddScreen.route)
            }
            val addVM = hiltViewModel<AddVM>(entry)
            AddScreen(navController = navController, addVM)
        }
    }
}