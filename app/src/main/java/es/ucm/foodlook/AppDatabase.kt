package es.ucm.foodlook

import androidx.room.Database
import androidx.room.RoomDatabase
import es.ucm.foodlook.entities.Recent
import es.ucm.foodlook.entities.RecentDao

@Database(entities = [Recent::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recentDao(): RecentDao
}
