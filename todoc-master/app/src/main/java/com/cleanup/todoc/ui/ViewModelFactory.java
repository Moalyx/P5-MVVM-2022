package com.cleanup.todoc.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.MainApplication;
import com.cleanup.todoc.data.DataBase;
import com.cleanup.todoc.data.Repository;
import com.cleanup.todoc.ui.main.DialogViewModel;
import com.cleanup.todoc.ui.main.MainViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;

    private final Executor executor = Executors.newFixedThreadPool(10);

    private final Repository repository;

    private ViewModelFactory(){
        DataBase dataBase = DataBase.getInstance(MainApplication.getApplication(), executor);

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
            return (T) new MainViewModel(repository, executor);
        }
        else if(modelClass.isAssignableFrom(DialogViewModel.class)){
            return (T) new DialogViewModel(repository, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
