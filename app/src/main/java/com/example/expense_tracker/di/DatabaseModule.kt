package com.example.expense_tracker.di

import android.content.Context
import androidx.room.Room
import com.example.expense_tracker.room.expenseDao
import com.example.expense_tracker.room.appDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideExpenseInstance(
        @ApplicationContext context: Context
        ): appDatabase {
        return Room.databaseBuilder(
            context,
            appDatabase::class.java,
            "Expense Database"
        ).build()
    }

    @Provides
    fun provideExpenseDao(
        appDatabase: appDatabase
    ): expenseDao {
        return appDatabase.getExpenseDao()
    }
}