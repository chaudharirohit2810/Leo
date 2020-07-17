package com.rohit2810.leo_kotlin.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.rohit2810.leo_kotlin.models.UserDatabaseModule

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userDatabaseModule: UserDatabaseModule)

    @Query("select * from user_database")
    fun getUser(): LiveData<UserDatabaseModule>

    @Query("delete from user_database")
    fun logoutUser()
}

@Database(entities = [UserDatabaseModule::class], version = 1)
abstract class LeoDatabase : RoomDatabase() {
    abstract val userDao: UserDao
}

private lateinit var INSTANCE: LeoDatabase

fun getDatabase(context: Context): LeoDatabase {
    synchronized(LeoDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                LeoDatabase::class.java,
                "userdatabase"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}