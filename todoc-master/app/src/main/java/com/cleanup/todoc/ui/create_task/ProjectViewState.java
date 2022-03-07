package com.cleanup.todoc.ui.create_task;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ProjectViewState {

    private final long projectId;
    private final String projectName;

    public ProjectViewState(long projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
    }

    public long getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectViewState that = (ProjectViewState) o;
        return projectId == that.projectId && Objects.equals(projectName, that.projectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, projectName);
    }

    @NonNull
    @Override
    public String toString() {
        return getProjectName();
    }
}
