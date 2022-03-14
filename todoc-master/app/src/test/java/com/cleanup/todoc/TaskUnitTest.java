package com.cleanup.todoc;

import com.cleanup.todoc.data.entity.Project;
import com.cleanup.todoc.data.entity.Task;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for tasks
 *
 * @author Gaëtan HERFRAY
 */
public class TaskUnitTest {
    @Test
    public void test_projects() {
        final Task task1 = new Task(1, 1, "task 1");
        final Task task2 = new Task(2, 2, "task 2");
        final Task task3 = new Task(3, 3, "task 3");

        final Project project1 = new Project(1, "Projet Tartampion", 0xFFEADAD1);
        final Project project2 = new Project(2, "Projet Lucidia", 0xFFB4CDBA);
        final Project project3 = new Project(3, "Projet Circus", 0xFFA3CED2);

        assertEquals(project1.getId(), task1.getProjectId());
        assertEquals(project2.getId(), task2.getProjectId());
        assertEquals(project3.getId(), task3.getProjectId());

    }

    @Test
    public void test_az_comparator() {
        final Task task1 = new Task(1, 1, "aaa");
        final Task task2 = new Task(2, 2, "zzz");
        final Task task3 = new Task(3, 3, "hhh");

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskAZComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task2);
    }

    @Test
    public void test_za_comparator() {
        final Task task1 = new Task(1, 1, "aaa");
        final Task task2 = new Task(2, 2, "zzz");
        final Task task3 = new Task(3, 3, "hhh");

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskZAComparator());

        assertSame(tasks.get(0), task2);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task1);
    }

    @Test
    public void test_recent_comparator() {
        final Task task1 = new Task(1, 1, "aaa");
        final Task task2 = new Task(2, 2, "zzz");
        final Task task3 = new Task(3, 3, "hhh");

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskRecentComparator());

        assertSame(tasks.get(0), task3);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task1);
    }

    @Test
    public void test_old_comparator() {
        final Task task1 = new Task(1, 1, "aaa");
        final Task task2 = new Task(2, 2, "zzz");
        final Task task3 = new Task(3, 3, "hhh");

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskOldComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task3);
    }
}