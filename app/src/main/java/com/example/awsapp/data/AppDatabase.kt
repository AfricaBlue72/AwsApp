package com.example.awsapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.awsapp.util.DATABASE_NAME

@Database(entities = [CognitoUser::class], version = 1)
@TypeConverters(AuthStatusConverter::class)
abstract class AppDatabase: RoomDatabase(){
    abstract fun cognitoUserDao(): CognitoUserDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase::class.java, DATABASE_NAME
                ).build().also { instance = it }
            }
        }
    }
}