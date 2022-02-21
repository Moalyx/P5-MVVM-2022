package com.cleanup.todoc.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTask (Task task);

    @Query("SELECT * FROM  Task")
    LiveData<List<Task>> getAllTask();

    @Delete
    void deleteTask(Task task);
}
