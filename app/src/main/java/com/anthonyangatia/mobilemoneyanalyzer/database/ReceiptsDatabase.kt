package com.anthonyangatia.mobilemoneyanalyzer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt

@Database(entities = [Receipt::class], version = 2, exportSchema = false)
abstract class ReceiptsDatabase: RoomDatabase() {

    abstract val receiptsDao:ReceiptsDao

    companion object{
        @Volatile
        private var INSTANCE:ReceiptsDatabase? = null

        fun getInstance(context: Context):ReceiptsDatabase{
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                    ReceiptsDatabase::class.java, "receipts_database")
                        .fallbackToDestructiveMigration().allowMainThreadQueries()//remove allowmain thread quesries after soring coroutines
                        .build()
                    INSTANCE = instance
                }
                return instance
            }

        }
    }
//    TODO:Write DB test as in codelabs
}