package com.cleanup.todoc.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

public class Repository {

    @NonNull
    private final ProjectDao projectDao;

    @NonNull
    private final TaskDao taskDao;



    public Repository(@NonNull ProjectDao projectDao, @NonNull TaskDao taskDao) {
        this.projectDao = projectDao;
        this.taskDao = taskDao;
    }

    public LiveData<List<Project>> getAllProjects(){
        return projectDao.getAllProjects();
    }

    public LiveData<List<Task>> getAllTask(){
        return taskDao.getAllTask();
    }

    public void insertTask(Task task){
        taskDao.insertTask(task);
    }

    public void deleteTask(Task task){
        taskDao.deleteTask(task);
    }






}
