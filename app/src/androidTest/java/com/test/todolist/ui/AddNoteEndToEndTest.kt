package com.test.todolist.ui

import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.test.todolist.di.LocalModule
import com.test.todolist.ui.addTask.AddScreen
import com.test.todolist.ui.home.HomeScreen
import com.test.todolist.ui.home.TasksVM
import com.test.todolist.ui.theme.ToDoListTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(LocalModule::class)
class AddNoteEndToEndTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
        composeRule.activity.setContent {
            ToDoListTheme {
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
                }
            }
        }
    }

    @Test
    fun addNewTask_getErrorForCategory_CreateNewCategory_AddNewTaskAgain() {

    }
}