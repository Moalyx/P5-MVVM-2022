package com.cleanup.todoc.data.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.data.DataBase;
import com.cleanup.todoc.data.entity.Project;
import com.cleanup.todoc.utils.LiveDataTestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ProjectDaoTest {

    private Project getFirstProject() {
        return new Project(0, "Projet Tartampion", 0xFFEADAD1);
    }

    private Project getSecondProject() {
        return new Project(0, "Projet Lucidia", 0xFFB4CDBA);
    }

    private Project getThirdProject() {
        return  new Project(0, "Projet Circus",0xFFA3CED2);
    }



    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private DataBase dataBase;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        dataBase = Room.inMemoryDatabaseBuilder(context, DataBase.class)
            .build();
    }

    @After
    public void closeDb() {
        dataBase.close();
    }

    @Test
    public void insertZeroGetZero() throws InterruptedException {
        List<Project> projects = LiveDataTestUtils.getValue(dataBase.projectDao().getAllProjects());
        assertEquals(projects.size(), 0);
        assertTrue(projects.isEmpty());
    }

    @Test
    public void insertOneProjectGetOneProject() throws InterruptedException {
        // Given
        dataBase.projectDao().insertProject(getFirstProject());

        // When
        List<Project> projects = LiveDataTestUtils.getValue(dataBase.projectDao().getAllProjects());

        // Then
        assertEquals(projects.size(), 1);
        Project project = projects.get(0);
        assertEquals(1, project.getId());
        assertEquals("Projet Tartampion", project.getName());
        assertEquals(0xFFEADAD1, project.getColor());
    }

    @Test
    public void insertTwoProjectsGetTwoProjects() throws InterruptedException {
        // Given
        dataBase.projectDao().insertProject(getFirstProject());
        dataBase.projectDao().insertProject(getSecondProject());

        // When
        List<Project> projects = LiveDataTestUtils.getValue(dataBase.projectDao().getAllProjects());

        // Then
        assertEquals(
            Arrays.asList(
                new Project(
                    1,
                    "Projet Tartampion",
                    0xFFEADAD1
                ),
                new Project(
                    2,
                    "Projet Lucidia",
                    0xFFB4CDBA
                )
            ),
            projects
        );
    }

    @Test
    public void insertThreeProjectsGetThreeProjects() throws InterruptedException {
        // Given
        dataBase.projectDao().insertProject(getFirstProject());
        dataBase.projectDao().insertProject(getSecondProject());
        dataBase.projectDao().insertProject(getThirdProject());

        //When
        List<Project> projects = LiveDataTestUtils.getValue(dataBase.projectDao().getAllProjects());

        //Then
        assertEquals(projects.size(), 3);

        assertEquals(Arrays.asList(
                new Project(
                        1,
                        "Projet Tartampion",
                        0xFFEADAD1
                ),
                new Project(
                        2,
                        "Projet Lucidia",
                        0xFFB4CDBA
                ),
                new Project(
                        3,
                        "Projet Circus",
                        0xFFA3CED2
                )
        ),
                projects
        );
    }


}

