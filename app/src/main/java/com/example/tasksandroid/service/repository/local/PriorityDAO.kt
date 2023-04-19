package com.example.tasksandroid.service.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tasksandroid.model.PriorityModel

@Dao
interface PriorityDAO {

    @Insert
    fun save(list: List<PriorityModel>)

    @Query("SELECT * FROM Priority")
    fun list(): List<PriorityModel>

    @Query("DELETE FROM Priority")
    fun clear()
}