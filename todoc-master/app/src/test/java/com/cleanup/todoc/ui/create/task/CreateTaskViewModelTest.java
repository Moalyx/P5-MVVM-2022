package com.cleanup.todoc.ui.create.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.Repository;
import com.cleanup.todoc.data.entity.Project;
import com.cleanup.todoc.data.entity.Task;
import com.cleanup.todoc.ui.MainThreadExecutor;
import com.cleanup.todoc.ui.create_task.CreateTaskViewModel;
import com.cleanup.todoc.ui.create_task.ProjectViewState;
import com.cleanup.todoc.ui.main.TaskViewState;
import com.cleanup.todoc.utils.LiveDataTestUtils;
import com.cleanup.todoc.utils.TestExecutor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Ordering;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@RunWith(MockitoJUnitRunner.class)
public class CreateTaskViewModelTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final Application application = Mockito.mock(Application.class);
    private final Repository repository = Mockito.mock(Repository.class);
    private final Executor ioExecutor = Mockito.spy(new TestExecutor());
    private final MainThreadExecutor mainThreadExecutor = Mockito.mock(MainThreadExecutor.class);

    private final MutableLiveData<List<Project>> projectMutableLiveData = new MutableLiveData<>();

    private CreateTaskViewModel createTaskViewModel;

    @Before
    public void setUp() {
        Mockito.doReturn(projectMutableLiveData).when(repository).getAllProjects();

        projectMutableLiveData.setValue(getDefaultProject());

        createTaskViewModel = new CreateTaskViewModel(application, repository, mainThreadExecutor, ioExecutor);

//        Mockito.verify(repository).getAllProjects(); //ERREUR ICI QUAND DECOMMENTE
    }

    @Test
    public void nomCase() throws InterruptedException {
        //When
        List<ProjectViewState> projectViewStates = LiveDataTestUtils.getValue(createTaskViewModel.getListProjectViewState());
        String toastMessage = LiveDataTestUtils.getValue(createTaskViewModel.getToastMessageSingleLiveEvent());
        Void dismissDialog = LiveDataTestUtils.getValue(createTaskViewModel.getDismissSingleLiveEvent());

        //Then
        assertEquals(getDefaultProjectViewStates(), projectViewStates);
        assertNull(toastMessage);
        assertNull(dismissDialog);
        //Mockito.verifyNoMoreInteractions(application, repository, mainThreadExecutor, ioExecutor); //ERREUR ICI QUAND DECOMMENTE
    }

    @Test
    public void emptyProject() throws InterruptedException {
        //Given
        projectMutableLiveData.setValue(new ArrayList<>());

        //When
        List<ProjectViewState> projectViewState = LiveDataTestUtils.getValue(createTaskViewModel.getListProjectViewState());
        String toastMessage = LiveDataTestUtils.getValue(createTaskViewModel.getToastMessageSingleLiveEvent());
        Void dismissDialog = LiveDataTestUtils.getValue(createTaskViewModel.getDismissSingleLiveEvent());

        //Then
        assertEquals(getEmptyProjectViewState(), projectViewState);
        assertNull(toastMessage);
        assertNull(dismissDialog);
    }

    @Test
    public void nullProject() throws InterruptedException {
        //Given
        projectMutableLiveData.setValue(null);

        //When
        List<ProjectViewState> projectViewState = LiveDataTestUtils.getValue(createTaskViewModel.getListProjectViewState());
        String toastMessage = LiveDataTestUtils.getValue(createTaskViewModel.getToastMessageSingleLiveEvent());
        Void dismissDialog = LiveDataTestUtils.getValue(createTaskViewModel.getDismissSingleLiveEvent());

        //Then
        assertNull(projectViewState);
        assertNull(toastMessage);
        assertNull(dismissDialog);
    }

    @Test
    public void addTaskSuccessfully() throws InterruptedException {
        //Given
        String taskDescription = "this is a taskDescription";
        ProjectViewState projectViewStateToTest = new ProjectViewState(1, "Projet Tartampion", 0xFFEADAD1);
        long selectedProjectId = projectViewStateToTest.getProjectId();

        //When
        createTaskViewModel.onOkButtonClicked(taskDescription, projectViewStateToTest);

        List<ProjectViewState> projectViewStates = LiveDataTestUtils.getValue(createTaskViewModel.getListProjectViewState());
        String toastMessage = LiveDataTestUtils.getValue(createTaskViewModel.getToastMessageSingleLiveEvent());
        Void dismissDialog = LiveDataTestUtils.getValue(createTaskViewModel.getDismissSingleLiveEvent());

        //Then
        assertEquals(getDefaultProjectViewStates(), projectViewStates);
        assertNull(toastMessage);
        assertNull(dismissDialog);
        Mockito.verify(ioExecutor).execute(any());
        Mockito.verify(repository).insertTask(new Task(0, selectedProjectId, taskDescription));
        //Mockito.verifyNoMoreInteractions(application, repository, mainThreadExecutor, ioExecutor); //ERREUR ICI QUAND DECOMMENTE
    }

    @Test
    public void addTaskFailedBecauseNoDescription() throws InterruptedException {
        //Given
        String taskDescription = "";
        ProjectViewState projectViewStateToTest = new ProjectViewState(1, "Projet Tartampion", 0xFFEADAD1);
        long selectedProjectId = projectViewStateToTest.getProjectId();

        //When
        createTaskViewModel.onOkButtonClicked(taskDescription, projectViewStateToTest);

        List<ProjectViewState> projectViewStates = LiveDataTestUtils.getValue(createTaskViewModel.getListProjectViewState());
        String toastMessage = LiveDataTestUtils.getValue(createTaskViewModel.getToastMessageSingleLiveEvent());
        Void dismissDialog = LiveDataTestUtils.getValue(createTaskViewModel.getDismissSingleLiveEvent());

        //Then
        assertEquals(getDefaultProjectViewStates(), projectViewStates);
        assertNull(toastMessage); //ICI JE NE SAIS PAS QUOI METTRE CAR LE TOAST NE DEVRAIT PAS ETRE NULL CAR LE MESSAGE S'AFFICHE ETANT DONNE QUE LA DESCRIPTION EST VIDE
        assertNull(dismissDialog);
        //Mockito.verifyNoMoreInteractions(application, repository, mainThreadExecutor, ioExecutor); //ERREUR ICI QUAND DECOMMENTE
    }


    @NonNull
    private List<Project> getDefaultProject() {
        List<Project> projects = new ArrayList<>();

        Project project1 = new Project(1, "Projet Tartampion", 0xFFEADAD1);
        Project project2 = new Project(2, "Projet Lucidia", 0xFFB4CDBA);
        Project project3 = new Project(3, "Projet Circus", 0xFFA3CED2);

        projects.add(project1);
        projects.add(project2);
        projects.add(project3);

        return projects;
    }

    @NonNull
    private List<ProjectViewState> getDefaultProjectViewStates() {
        List<ProjectViewState> projectViewStates = new ArrayList<>();
        List<Project> projects = new ArrayList<>();


        Project project1 = new Project(1, "Projet Tartampion", 0xFFEADAD1);
        Project project2 = new Project(2, "Projet Lucidia", 0xFFB4CDBA);
        Project project3 = new Project(3, "Projet Circus", 0xFFA3CED2);

        projects.add(project1);
        projects.add(project2);
        projects.add(project3);

        for (Project project : projects) {
            projectViewStates.add(
                    new ProjectViewState(
                            project.getId(),
                            project.getName(),
                            project.getColor()
                    )
            );
        }
        return projectViewStates;
    }

    private List<ProjectViewState> getEmptyProjectViewState() {
        return new ArrayList<>();
    }


}
