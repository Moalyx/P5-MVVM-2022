package com.cleanup.todoc.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(foreignKeys = @ForeignKey(entity = Project.class, parentColumns = "id", childColumns = "projectId"))
public class Task {

    @PrimaryKey(autoGenerate = true)
    private final long id;

    @ColumnInfo(index = true)
    private final long projectId;

    private final String name;

    public Task(long id, long projectId, String name) {
        this.id = id;
        this.projectId = projectId;
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && projectId == task.projectId && Objects.equals(name, task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectId, name);
    }

    @NonNull
    @Override
    public String toString() {
        return "Task{" +
            "id=" + id +
            ", projectId=" + projectId +
            ", name='" + name + '\'' +
            '}';
    }

    public static class TaskAZComparator {
    }
}
