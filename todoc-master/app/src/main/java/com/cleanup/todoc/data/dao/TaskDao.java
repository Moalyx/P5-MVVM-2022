package com.cleanup.todoc.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.data.entity.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insertTask(Task task);

    @Query("SELECT * FROM task")
    LiveData<List<Task>> getAllTask();

    @Query("DELETE FROM task WHERE id=:taskId")
    void deleteTaskById(long taskId);
}
