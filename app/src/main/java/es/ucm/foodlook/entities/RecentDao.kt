package es.ucm.foodlook.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecentDao {
    @Query("SELECT * FROM recent")
    fun getAll(): List<Recent>

    @Query("SELECT * FROM recent WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Recent

    @Query("SELECT * FROM recent WHERE id LIKE :id LIMIT 1")
    fun findById(id: String): Recent

    @Insert
    fun insertAll(vararg recents: Recent)

    @Delete
    fun delete(recent: Recent)
}