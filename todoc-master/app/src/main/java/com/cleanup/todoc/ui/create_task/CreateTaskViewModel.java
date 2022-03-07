package com.cleanup.todoc.ui.create_task;

import android.content.Context;

import androidx.annotation.NonNull;
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
    private final MainThreadExecutor mainThreadExecutor;
    private final Executor ioExecutor;

    private final SingleLiveEvent<String> toastMessageSingleLiveEvent = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> dismissSingleLiveEvent = new SingleLiveEvent<>();

    public CreateTaskViewModel(
        Context context,
        @NonNull Repository repository,
        MainThreadExecutor mainThreadExecutor,
        Executor ioExecutor
    ) {
        this.context = context;
        this.repository = repository;
        this.mainThreadExecutor = mainThreadExecutor;
        this.ioExecutor = ioExecutor;
    }

    public void onProjectSelected(Project project) {
        repository.onProjectSelected(project);
    }

    public void onTaskChanged(String name) {
        repository.onTaskChanged(name);
    }

    public void onAddTaskButtonClick(long projectId, @NonNull String name, long creationTimestamp) {
        ioExecutor.execute(new Runnable() {
            @Override
            public void run() {
                repository.insertTask(new Task(0, projectId, name));
            }
        });
    }

    public LiveData<List<ProjectViewState>> getListProjectViewState() {
        return Transformations.map(repository.getAllProjects(), new Function<List<Project>, List<ProjectViewState>>() {
            @Override
            public List<ProjectViewState> apply(List<Project> projects) {
                List<ProjectViewState> projectViewStates = new ArrayList<>();
                for (Project project : projects) {
                    projectViewStates.add(
                        new ProjectViewState(
                            project.getId(),
                            project.getName()
                        )
                    );
                }
                return projectViewStates;
            }
        });
    }

    public SingleLiveEvent<String> getToastMessageSingleLiveEvent() {
        return toastMessageSingleLiveEvent;
    }

    public SingleLiveEvent<Void> getDismissSingleLiveEvent() {
        return dismissSingleLiveEvent;
    }

    public void onOkButtonClicked(String taskName, ProjectViewState projectViewState) {
        if (taskName.isEmpty()) {
            toastMessageSingleLiveEvent.setValue(context.getString(R.string.app_name) + " 3");
            return;
        }

        ioExecutor.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                repository.insertTask(new Task(0, projectViewState.getProjectId(), taskName));

                mainThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        dismissSingleLiveEvent.call();
                    }
                });
            }
        });
//
//        if (taskNameEditText != null && projectSpinner != null) {
//            String taskName = taskNameEditText.getText().toString();
//
//            Project project = null;
//            long projectId = 0;
//            if (projectSpinner.getSelectedItem() instanceof Project) {
//                project = (Project) projectSpinner.getSelectedItem();
//                projectId = project.getId();
//            }
//
//            if (taskName.trim().isEmpty()) {
//                taskNameEditText.setError(getString(R.string.empty_task_name));
//            } else if (project != null) {
//                long timeStamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
//
//                viewModel.onAddTaskButtonClick(projectId, taskName, timeStamp);
//
//                // dialogInterface.dismiss();
////                    } else {
////                        dialogInterface.dismiss();
////                    }
////                } else {
////                    dialogInterface.dismiss();
//            }
    }

}

