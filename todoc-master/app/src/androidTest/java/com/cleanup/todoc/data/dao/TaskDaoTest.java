package com.cleanup.todoc.data.dao;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import androidx.room.Room;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.data.DataBase;
import com.cleanup.todoc.data.entity.Project;
import com.cleanup.todoc.data.entity.Task;
import com.cleanup.todoc.utils.LiveDataTestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;


@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    private DataBase database;

    private static final Project project1 = new Project(1, "Tartampion", 0xFFEADAD1);
    private static final Project project2 = new Project(2, "Projet Lucidia", 0xFFB4CDBA);
    private static final long projectId1 = project1.getId();
    private static final long projectId2 = project2.getId();
    private static final Task task1 = new Task(1, projectId1, "première tache");
    private static final Task task2 = new Task(2, projectId2, "deuxième tache");

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() {
        Context context = ApplicationProvider.getApplicationContext();
        this.database = Room.inMemoryDatabaseBuilder(context,
                DataBase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        this.database.close();
    }

    @Test
    public void insertOneTaskGetOneTask() throws InterruptedException {
        List<Project> projects = LiveDataTestUtils.getValue(this.database.projectDao().getAllProjects());
        List<Task> tasks = LiveDataTestUtils.getValue(database.taskDao().getAllTask());
        assertEquals(projects.size(), 0);
        assertEquals(tasks.size(), 0);

        database.projectDao().insertProject(project1);
        projects = LiveDataTestUtils.getValue(database.projectDao().getAllProjects());
        assertEquals(projects.size(), 1);

        assertTrue(projects.get(0).getId() == project1.getId()
                && projects.get(0).getName().equals(project1.getName())
                && projects.get(0).getColor() == project1.getColor()
        );

        database.taskDao().insertTask(task1);
        tasks = LiveDataTestUtils.getValue(database.taskDao().getAllTask());
        assertEquals(tasks.size(), 1);

        assertTrue(tasks.get(0).getName().equals(task1.getName())
                && tasks.get(0).getId() == task1.getId()
                && tasks.get(0).getProjectId() == project1.getId()
        );
    }

    @Test
    public void insertTwoTasksGetTwoTasks() throws InterruptedException {
        List<Project> projects = LiveDataTestUtils.getValue(this.database.projectDao().getAllProjects());
        List<Task> tasks = LiveDataTestUtils.getValue(database.taskDao().getAllTask());
        assertEquals(projects.size(), 0);
        assertEquals(tasks.size(), 0);

        database.projectDao().insertProject(project1);
        database.taskDao().insertTask(task1);
        database.projectDao().insertProject(project2);
        database.taskDao().insertTask(task2);

        assertEquals(projects.size(), 2);
        assertEquals(tasks.size(), 2);

        assertEquals(task1.getProjectId(), projectId1);
        assertEquals(task2.getProjectId(), projectId2);

        assertTrue(projects.get(0).getId() == project1.getId()
                && projects.get(0).getName().equals(project1.getName())
                && projects.get(0).getColor() == project1.getColor()
        );

        assertTrue(projects.get(1).getId() == project2.getId()
                && projects.get(1).getName().equals(project2.getName())
                && projects.get(1).getColor() == project2.getColor()
        );

        assertTrue(tasks.get(0).getName().equals(task1.getName())
                && tasks.get(0).getId() == task1.getId()
                && tasks.get(0).getProjectId() == project1.getId()
        );

        assertTrue(tasks.get(1).getName().equals(task2.getName())
                && tasks.get(1).getId() == task2.getId()
                && tasks.get(1).getProjectId() == project2.getId()
        );
    }

    @Test
    public void insertOneTaskDeleteOneGetOneGetZero() throws InterruptedException {
        List<Project> projects = LiveDataTestUtils.getValue(this.database.projectDao().getAllProjects());
        List<Task> tasks = LiveDataTestUtils.getValue(database.taskDao().getAllTask());
        assertEquals(projects.size(), 0);
        assertEquals(tasks.size(), 0);

        database.projectDao().insertProject(project1);
        database.taskDao().insertTask(task1);

        assertEquals(projects.size(), 1);
        assertEquals(tasks.size(), 1);

        database.taskDao().deleteTaskById(task1.getId());

        assertEquals(tasks.size(), 0);
        assertTrue(tasks.isEmpty());
    }


}
