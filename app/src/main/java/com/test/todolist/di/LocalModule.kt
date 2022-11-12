package com.test.todolist.di

import android.content.Context
import androidx.room.Room
import com.test.todolist.data.local.room.AppDatabase
import com.test.todolist.data.local.room.ToDoCategoryDao
import com.test.todolist.data.local.room.ToDoEntryDao
import com.test.todolist.data.local.room.ToDoEntryToDoCategoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideToDoEntryDao(
        appDatabase: AppDatabase
    ): ToDoEntryDao =
        appDatabase.toDoEntryDao()

    @Singleton
    @Provides
    fun provideToDoCategoryDao(
        appDatabase: AppDatabase
    ): ToDoCategoryDao =
        appDatabase.toDoCategoryDao()

    @Singleton
    @Provides
    fun provideToDoEntryToDoCategoryDao(
        appDatabase: AppDatabase
    ): ToDoEntryToDoCategoryDao =
        appDatabase.toDoEntryToDoCategoryDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "To Do Entries"
        ).build()
    }
}
