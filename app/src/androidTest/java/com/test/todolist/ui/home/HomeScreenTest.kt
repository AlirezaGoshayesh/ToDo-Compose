package com.test.todolist.ui.home

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.test.todolist.di.LocalModule
import com.test.todolist.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.*
import org.junit.Rule

@HiltAndroidTest
@UninstallModules(LocalModule::class)
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

//    @Before
//    fun setup(){
//        hiltRule.inject()
//        composeRule.setContent {
//            val navController = rememberNavController()
//            ToDoListTheme {
//                NavHost(navController = navController, startDestination = Screen.HomeScreen.route){
//                    composable(route = Screen.HomeScreen.route){
//                        HomeScreen(navController = navController, viewModel = )
//                    }
//                }
//            }
//        }
//    }
}