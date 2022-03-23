package com.cleanup.todoc.ui.create_task;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.R;
import com.cleanup.todoc.data.Repository;
import com.cleanup.todoc.data.entity.Project;
import com.cleanup.todoc.data.entity.Task;
import com.cleanup.todoc.ui.MainThreadExecutor;
import com.cleanup.todoc.ui.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class CreateTaskViewModel extends ViewModel {

    @NonNull
    private final Repository repository;
    private final Context context;
    private final Executor mainThreadExecutor;
    private final Executor ioExecutor;


    private final SingleLiveEvent<String> toastMessageSingleLiveEvent = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> dismissSingleLiveEvent = new SingleLiveEvent<>();

    private final LiveData<List<ProjectViewState>> viewStateLiveData;

    public CreateTaskViewModel(
            Context context,
            @NonNull Repository repository,
            Executor mainThreadExecutor,
            Executor ioExecutor
    ) {
        this.context = context;
        this.repository = repository;
        this.mainThreadExecutor = mainThreadExecutor;
        this.ioExecutor = ioExecutor;

        viewStateLiveData = Transformations.map(repository.getAllProjects(), new Function<List<Project>, List<ProjectViewState>>() {
            @Override
            public List<ProjectViewState> apply(List<Project> projects) {
                List<ProjectViewState> projectViewStates = new ArrayList<>();
                for (Project project : projects) {
                    projectViewStates.add(
                            new ProjectViewState(
                                    project.getId(),
                                    project.getName(),
                                    project.getColor()
                            )
                    );
                }
                return projectViewStates;
            }
        });
    }

    public LiveData<List<ProjectViewState>> getListProjectViewState() {
        return viewStateLiveData;
    }

    public SingleLiveEvent<String> getToastMessageSingleLiveEvent() {
        return toastMessageSingleLiveEvent;
    }

//    public int[] getColorForSpinner (){
//        List<ProjectViewState> projects;
//        projectLiveData = CreateTaskViewModel.this.getListProjectViewState(); // repository.getAllProjects();
//        projects = projectLiveData.getValue();
//
//        int [] colorList = new int[3];
//        for (int i = 0; i < projects.size() ; i++){
//            colorList[i] = projects.get(i).getColor();
//        }
//        return colorList;
//    }
//
//    public String[] getNameForSpinner (){
//        List<ProjectViewState> projects;
//        projectLiveData = CreateTaskViewModel.this.getListProjectViewState(); // repository.getAllProjects();
//        projects = projectLiveData.getValue();
//
//        String [] nameList = new String [3];
//        for (int i = 0; i < projects.size() ; i++){
//            nameList[i] = projects.get(i).getProjectName();
//        }
//        return nameList;
//    }



    public SingleLiveEvent<Void> getDismissSingleLiveEvent() {
        return dismissSingleLiveEvent;
    }

    public void onOkButtonClicked(long projectId, String taskName) {
        if (taskName.isEmpty()) {
            toastMessageSingleLiveEvent.setValue(context.getString(R.string.empty_task));
            return;
        }

        ioExecutor.execute(new Runnable() {
            @Override
            public void run() {

//                try {
//                    Thread.sleep(3_000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                repository.insertTask(new Task(0, projectId, taskName));

                mainThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        dismissSingleLiveEvent.call();
                    }
                });
            }
        });

    }

}

