package com.cleanup.todoc.ui.main;


import androidx.annotation.ColorInt;

import java.util.Objects;

public class TaskViewState {

    private final long id;

    private final String name;

    private final String projectName;

    @ColorInt
    private final int colorProject;

    public TaskViewState(long id, String name, String projectName, @ColorInt int colorProject) {
        this.id = id;
        this.name = name;
        this.projectName = projectName;
        this.colorProject = colorProject;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskViewState that = (TaskViewState) o;
        return id == that.id && colorProject == that.colorProject && name.equals(that.name) && projectName.equals(that.projectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, projectName, colorProject);
    }

    @Override
    public String toString() {
        return "TaskViewState{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", projectName='" + projectName + '\'' +
            ", colorProject=" + colorProject +
            '}';
    }
}
