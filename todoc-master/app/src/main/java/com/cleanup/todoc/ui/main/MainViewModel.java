package com.cleanup.todoc.ui.main;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.data.Repository;
import com.cleanup.todoc.data.entity.Task;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executor;

public class MainViewModel extends ViewModel {

    @NonNull
    private final Repository repository;
    private final Executor executor;
    private final MutableLiveData<SortMethod> currentSortMethodMutableLiveData = new MutableLiveData<>();

    private final MediatorLiveData<List<TaskViewState>> mediatorLiveData = new MediatorLiveData<>();

    public MainViewModel(@NonNull Repository repository, Executor executor) {
        this.repository = repository;
        this.executor = executor;

        currentSortMethodMutableLiveData.setValue(SortMethod.NONE);

        LiveData<List<Task>> tasksLiveData = repository.getAllTask();

        mediatorLiveData.addSource(tasksLiveData, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                combine(tasks, currentSortMethodMutableLiveData.getValue());
            }
        });

        mediatorLiveData.addSource(currentSortMethodMutableLiveData, new Observer<SortMethod>() {
            @Override
            public void onChanged(SortMethod sortMethod) {
                combine(tasksLiveData.getValue(), sortMethod);
            }
        });

        // TODO 3ème source (les projects !)
    }

    public LiveData<List<TaskViewState>> getViewStateLiveData() {
        return mediatorLiveData;
    }

    private void combine(@Nullable List<Task> tasks, @Nullable SortMethod sortMethod) {
        if (tasks == null || sortMethod == null) {
            return;
        }

        List<Task> sortableTasks = new ArrayList<>(tasks);
        List<TaskViewState> taskViewStates = new ArrayList<>();

        switch (sortMethod) {
            case ALPHABETICAL:
                Collections.sort(sortableTasks, new TaskAZComparator());
                break;
            case ALPHABETICAL_INVERTED:
                Collections.sort(sortableTasks, new TaskZAComparator());
                break;
            case RECENT_FIRST:
                Collections.sort(sortableTasks, new TaskRecentComparator());
                break;
            case OLD_FIRST:
                Collections.sort(sortableTasks, new TaskOldComparator());
                break;
        }

        for (Task task : sortableTasks) {
            taskViewStates.add(
                new TaskViewState(
                    task.getId(),
                    task.getName(),
                    "PROJECT NAME",
                    Color.parseColor("#FF0088"),
                    task.getCreationTimestamp()
                )
            );
        }

        mediatorLiveData.setValue(taskViewStates);
    }

    public void addRandomTask() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int rolled = new Random().nextInt(3) + 1;

                repository.insertTask(new Task(
                    0,
                    rolled,
                    UUID.randomUUID().toString(),
                    LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
                ));
            }
        });
    }

    public void deleteTask(long id) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                repository.deleteTaskById(id);
            }
        });
    }

    public void onSortMethodChanged(SortMethod sortMethod) {
        currentSortMethodMutableLiveData.setValue(sortMethod);
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

    enum SortMethod {

        ALPHABETICAL,

        ALPHABETICAL_INVERTED,

        RECENT_FIRST,

        OLD_FIRST,

        NONE
    }


    public static class TaskAZComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return left.getName().compareTo(right.getName());
        }
    }

    public static class TaskZAComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return right.getName().compareTo(left.getName());
        }
    }

    public static class TaskRecentComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return (int) (right.getCreationTimestamp() - left.getCreationTimestamp());
        }
    }

    public static class TaskOldComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return (int) (left.getCreationTimestamp() - right.getCreationTimestamp());
        }
    }


    // TODO A partir côté VM du AddTask(Dialog)Fragment
    public void onAddTaskButtonClick(long projectId, @NonNull String name, long creationTimestamp) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                repository.insertTask(new Task(0, projectId, name, creationTimestamp));
            }
        });
    }


}
