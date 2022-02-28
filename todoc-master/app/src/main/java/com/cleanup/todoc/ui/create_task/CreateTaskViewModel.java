package com.cleanup.todoc.ui.create_task;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.data.Repository;
import com.cleanup.todoc.data.entity.Project;
import com.cleanup.todoc.data.entity.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class CreateTaskViewModel extends ViewModel {

    @NonNull
    private final Repository repository;
    private final Executor executor;

    public CreateTaskViewModel(@NonNull Repository repository, Executor executor) {
        this.repository = repository;
        this.executor = executor;
    }

    public void onProjectSelected(Project project) {
        repository.onProjectSelected(project);
    }

    public void onTaskChanged(String name) {
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

}
