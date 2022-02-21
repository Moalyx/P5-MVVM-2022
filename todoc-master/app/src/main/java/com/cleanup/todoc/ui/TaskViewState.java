package com.cleanup.todoc.ui;


import java.util.Objects;

public class TaskViewState {


    private long id;

    private long projectId;

    private String name;

    private String projectName;

    private int colorProject;

    private long creationTimestamp;

    public TaskViewState(long id, long projectId, String name, String projectName, int colorProject, long creationTimestamp) {
        this.id = id;
        this.projectId = projectId;
        this.name = name;
        this.projectName = projectName;
        this.colorProject = colorProject;
        this.creationTimestamp = creationTimestamp;
    }

    public long getId() {
        return id;
    }

    public long getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public String getProjectName() {
        return projectName;
    }

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
        return id == that.id && projectId == that.projectId && colorProject == that.colorProject && creationTimestamp == that.creationTimestamp && name.equals(that.name) && projectName.equals(that.projectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectId, name, projectName, colorProject, creationTimestamp);
    }

    @Override
    public String toString() {
        return "TaskViewState{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", name='" + name + '\'' +
                ", projectName='" + projectName + '\'' +
                ", colorProject=" + colorProject +
                ", creationTimestamp=" + creationTimestamp +
                '}';
    }
}
