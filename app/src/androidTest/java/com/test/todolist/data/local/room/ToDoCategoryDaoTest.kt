package com.test.todolist.data.local.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.test.todolist.data.models.ToDoCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ToDoCategoryDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var toDoCategoryDao: ToDoCategoryDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        toDoCategoryDao = database.toDoCategoryDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertCategory() = runTest {
        val category = ToDoCategory(name = "name", color = 123456, id = 1)
        toDoCategoryDao.insert(category)

        val allCategories = toDoCategoryDao.getAllCategories()

        assertThat(allCategories).contains(category)
    }

    @Test
    fun deleteCategory() = runTest {
        val category = ToDoCategory(name = "name", color = 123456, id = 1)
        toDoCategoryDao.insert(category)
        toDoCategoryDao.delete(category)

        val allCategories = toDoCategoryDao.getAllCategories()

        assertThat(allCategories).doesNotContain(category)
    }

}