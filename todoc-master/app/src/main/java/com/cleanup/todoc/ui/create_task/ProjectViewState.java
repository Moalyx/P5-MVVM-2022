package com.cleanup.todoc.ui.create_task;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ProjectViewState {

    private final long id;
    private final String projectName;

    public ProjectViewState(long id, String projectName) {
        this.id = id;
        this.projectName = projectName;
    }

    public long getId() {
        return id;
    }

    public String getProjectName() {
        return projectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectViewState that = (ProjectViewState) o;
        return id == that.id && Objects.equals(projectName, that.projectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectName);
    }

    @NonNull
    @Override
    public String toString() {
        return getProjectName();
    }
}
