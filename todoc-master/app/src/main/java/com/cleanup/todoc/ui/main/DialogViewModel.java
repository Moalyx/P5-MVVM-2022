package com.cleanup.todoc.ui.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.R;
import com.cleanup.todoc.data.Repository;
import com.cleanup.todoc.data.entity.Project;
import com.cleanup.todoc.data.entity.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

public class DialogViewModel extends ViewModel {

    @NonNull
    private final Repository repository;
    private final Executor executor;
    private LiveData<List<Project>> projectLiveData;
    List<Project> projects = new ArrayList<>();

    public DialogViewModel(@NonNull Repository repository, Executor executor) {
        this.repository = repository;
        this.executor = executor;
    }

//    public LiveData<List<Project>> getAllProjects() {
//        return repository.getAllProjects();
//    }

    public List<Project> getallTheProjects(){
        projectLiveData = repository.getAllProjects();
        projects = projectLiveData.getValue();
        return projects;
    }

    public void onProjectSelected(Project project) {
        repository.onProjectSelected(project);
    }

    public void onTaskChanged(String name){
        repository.onTaskChanged(name);
    }

    public void onAddTaskButtonClick(long projectId, @NonNull String name, long creationTimestamp) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                repository.insertTask(new Task(0, projectId, name, creationTimestamp));
            }
        });
    }

    public LiveData<List<ProjectViewState>> getprojectViewState(){
        return Transformations.map(repository.getAllProjects(), new Function<List<Project>, List<ProjectViewState>>() {
            @Override
            public List<ProjectViewState> apply(List<Project> projects) {
                List<ProjectViewState> projectViewStates = new ArrayList<>();
                for (Project project : projects){
                    projectViewStates.add(new ProjectViewState(project.getName()));
                }
                return projectViewStates;
            }
        });
    }

    public List<ProjectViewState> getListProjectViewState(){
        LiveData<List<ProjectViewState>> projectViewStatesLiveData = getprojectViewState();
        return projectViewStatesLiveData.getValue();
    }



}
