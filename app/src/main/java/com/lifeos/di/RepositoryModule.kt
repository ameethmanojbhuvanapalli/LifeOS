package com.lifeos.di

import com.lifeos.data.repository.TodoRepositoryImpl
import com.lifeos.domain.repository.TodoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindTodoRepository(impl: TodoRepositoryImpl): TodoRepository
}
