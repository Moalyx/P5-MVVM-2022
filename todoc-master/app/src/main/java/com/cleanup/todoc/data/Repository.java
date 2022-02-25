package com.cleanup.todoc.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.cleanup.todoc.data.dao.ProjectDao;
import com.cleanup.todoc.data.dao.TaskDao;
import com.cleanup.todoc.data.entity.Project;
import com.cleanup.todoc.data.entity.Task;

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

    public LiveData<List<Project>> getAllProjects() {
        return projectDao.getAllProjects();
    }

    public LiveData<List<Task>> getAllTask() {
        return taskDao.getAllTask();
    }

    public void insertTask(Task task) {
        taskDao.insertTask(task);
    }

    public void deleteTaskById(long id) {
        taskDao.deleteTaskById(id);
    }

    public void onProjectSelected(Project project) {
        String projectName = project.getName();
    }

    public void onTaskChanged(String name){}

    public void onAddTaskButtonClick(Task task){}


}
