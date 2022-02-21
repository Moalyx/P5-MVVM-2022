package com.cleanup.todoc.ui.main;


import androidx.annotation.ColorInt;

import java.util.Objects;

public class TaskViewState {

    private final long id;

    private final String name;

    private final String projectName;

    @ColorInt
    private final int colorProject;

    private final long creationTimestamp;

    public TaskViewState(long id, String name, String projectName, @ColorInt int colorProject, long creationTimestamp) {
        this.id = id;
        this.name = name;
        this.projectName = projectName;
        this.colorProject = colorProject;
        this.creationTimestamp = creationTimestamp;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProjectName() {
        return projectName;
    }

    @ColorInt
    public int getColorProject() {
        return colorProject;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskViewState that = (TaskViewState) o;
        return id == that.id && colorProject == that.colorProject && creationTimestamp == that.creationTimestamp && name.equals(that.name) && projectName.equals(that.projectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, projectName, colorProject, creationTimestamp);
    }

    @Override
    public String toString() {
        return "TaskViewState{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", projectName='" + projectName + '\'' +
            ", colorProject=" + colorProject +
            ", creationTimestamp=" + creationTimestamp +
            '}';
    }
}
