package com.cleanup.todoc.model;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

public class MainViewModel extends ViewModel {

    @NonNull
    private final Repository repository;
    private final Executor executor;
    private MutableLiveData<List<Project>> allProjMut = new MutableLiveData<>();
    private MutableLiveData<List<Task>> allTaskMut = new MutableLiveData<>();
    private MutableLiveData<List<TaskViewState>> allTaskToSort = new MutableLiveData<>();
    private List<Project> projects = new ArrayList<>();
    private List<Task> taskList = new ArrayList<>();
    long currentMaxTaskID = 0;
    @NonNull
    private final SortMethod sortMethod = SortMethod.NONE;
    MediatorLiveData<TaskViewState> taskViewStateMediatorLiveData;

    public MainViewModel(@NonNull Repository repository, Executor executor) {
        this.repository = repository;
        this.executor = executor;


    }

    public void getTasks(List<Task> tasks){
        taskList = tasks;
        sortTasks(taskList);
    }

    public LiveData<List<Task>> sortTasks(List<Task> tasks) {
        tasks = taskList;
        switch (sortMethod) {
            case ALPHABETICAL:
                Collections.sort(tasks, new Task.TaskAZComparator());
                break;
            case ALPHABETICAL_INVERTED:
                Collections.sort(tasks, new Task.TaskZAComparator());
                break;
            case RECENT_FIRST:
                Collections.sort(tasks, new Task.TaskRecentComparator());
                break;
            case OLD_FIRST:
                Collections.sort(tasks, new Task.TaskOldComparator());
                break;
        }
        allTaskMut.setValue(tasks);
        return allTaskMut;
    }

    public LiveData<List<Project>> getAllProjects() {
        return repository.getAllProjects();
    }

    public LiveData<List<Task>> getAllTask() {
        return repository.getAllTask();
    }

    public void deleteTask(long id) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (Iterator<Task> iterator = taskList.iterator(); iterator.hasNext();){
                    Task task = iterator.next();
                    if(task.getId() == id){
                        iterator.remove();
                        repository.deleteTask(task);
                    }
                }
            }
        });
    }

    public void onAddTaskButtonClick(long projectId, @NonNull String name, long creationTimestamp) {
        currentMaxTaskID++;
        Task task = new Task(currentMaxTaskID, projectId, name, creationTimestamp);
        insertTask(task);
    }

    public void insertTask(Task task) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                repository.insertTask(task);
            }
        });
    }

    public LiveData<List<TaskViewState>> getListTaskViewstate() {
        return Transformations.map(allTaskMut, new Function<List<Task>, List<TaskViewState>>() {
            @Override
            public List<TaskViewState> apply(List<Task> tasks) {
                List<TaskViewState> taskViewStates = new ArrayList<>();
                for (Task task : tasks) {
                    switch (sortMethod) {
                        case ALPHABETICAL:
                            Collections.sort(tasks, new Task.TaskAZComparator());
                            break;
                        case ALPHABETICAL_INVERTED:
                            Collections.sort(tasks, new Task.TaskZAComparator());
                            break;
                        case RECENT_FIRST:
                            Collections.sort(tasks, new Task.TaskRecentComparator());
                            break;
                        case OLD_FIRST:
                            Collections.sort(tasks, new Task.TaskOldComparator());
                            break;
                    }

                    taskViewStates.add(new TaskViewState(task.getId(),
                            task.getProjectId(), task.getName(),
                            task.getProject().toString(),
                            task.getProject().getColor(),
                            task.getCreationTimestamp()));
                }
                allTaskToSort.setValue(taskViewStates);
                return taskViewStates;


            }
        });
    }

//    public LiveData<List<TaskViewState>> getListTaskViewstates() {
//        getListTaskViewstate();
//        List<TaskViewState> taskViewStates = allTaskToSort.getValue() ;
//        switch (sortMethod) {
//            case ALPHABETICAL:
//                Collections.sort(tasks, new Task.TaskAZComparator());
//                break;
//            case ALPHABETICAL_INVERTED:
//                Collections.sort(tasks, new Task.TaskZAComparator());
//                break;
//            case RECENT_FIRST:
//                Collections.sort(tasks, new Task.TaskRecentComparator());
//                break;
//            case OLD_FIRST:
//                Collections.sort(tasks, new Task.TaskOldComparator());
//                break;
//        }
//
//    }

    private enum SortMethod {

        ALPHABETICAL,

        ALPHABETICAL_INVERTED,

        RECENT_FIRST,

        OLD_FIRST,

        NONE
    }

}
