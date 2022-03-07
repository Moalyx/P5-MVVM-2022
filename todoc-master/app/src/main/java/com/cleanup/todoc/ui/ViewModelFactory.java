package com.cleanup.todoc.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.MainApplication;
import com.cleanup.todoc.data.DataBase;
import com.cleanup.todoc.data.Repository;
import com.cleanup.todoc.ui.create_task.CreateTaskViewModel;
import com.cleanup.todoc.ui.main.MainViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;

    private final MainThreadExecutor mainThreadExecutor = new MainThreadExecutor();
    private final Executor ioExecutor = Executors.newFixedThreadPool(10);

    private final Repository repository;

    private ViewModelFactory(){
        DataBase dataBase = DataBase.getInstance(MainApplication.getApplication(), ioExecutor);

        repository = new Repository(dataBase.projectDao(), dataBase.taskDao());
    }
    public static ViewModelFactory getInstance(){
        if (factory == null){
            synchronized (ViewModelFactory.class){
                if (factory == null){
                    factory = new ViewModelFactory();
                }
            }
        }
        return factory;

    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(repository, ioExecutor);
        }
        else if(modelClass.isAssignableFrom(CreateTaskViewModel.class)){
            return (T) new CreateTaskViewModel(
                MainApplication.getApplication(),
                repository,
                mainThreadExecutor,
                ioExecutor
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
