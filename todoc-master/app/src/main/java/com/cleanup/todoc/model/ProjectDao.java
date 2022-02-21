package com.cleanup.todoc.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import androidx.room.Insert;

import androidx.room.Query;

import java.util.List;

@Dao
public interface ProjectDao {

    @Insert
    void insertProject(Project project);

    @Query("SELECT * FROM project")
    LiveData<List<Project>> getAllProjects();

}
