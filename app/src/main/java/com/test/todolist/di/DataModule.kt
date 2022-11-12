package com.test.todolist.di

import com.test.todolist.data.DataSource
import com.test.todolist.data.ErrorHandler
import com.test.todolist.data.RepositoryImpl
import com.test.todolist.data.local.LocalDataSourceImpl
import com.test.todolist.data.local.room.ToDoCategoryDao
import com.test.todolist.data.local.room.ToDoEntryDao
import com.test.todolist.data.local.room.ToDoEntryToDoCategoryDao
import com.test.todolist.domain.Repository
import com.test.todolist.domain.exceptions.IErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideRepository(
        localDataSourceImpl: LocalDataSourceImpl
    ): Repository {
        return RepositoryImpl(localDataSourceImpl)
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(
        toDoEntryDao: ToDoEntryDao,
        toDoCategoryDao: ToDoCategoryDao,
        toDoEntryToDoCategoryDao: ToDoEntryToDoCategoryDao
    ): DataSource.Local {
        return LocalDataSourceImpl(toDoEntryDao, toDoCategoryDao, toDoEntryToDoCategoryDao)
    }

    @Singleton
    @Provides
    fun provideErrorHandler(): IErrorHandler {
        return ErrorHandler()
    }
}
