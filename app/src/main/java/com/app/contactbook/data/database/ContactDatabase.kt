package com.app.contactbook.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.contactbook.data.entity.ContactEntity

@Database(
    entities = [ContactEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ContactDatabase : RoomDatabase() {
    
    abstract fun contactDao(): ContactDao
    
    companion object {
        @Volatile
        private var INSTANCE: ContactDatabase? = null
        
        fun getDatabase(context: Context): ContactDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDatabase::class.java,
                    "contact_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
