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

import com.cleanup.todoc.R;
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
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Ordering;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

@RunWith(MockitoJUnitRunner.class)
public class CreateTaskViewModelTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final Application application = Mockito.mock(Application.class, new RuntimeExceptionAnswer());
    private final Repository repository = Mockito.mock(Repository.class);
    private final TestExecutor ioExecutor = Mockito.spy(new TestExecutor());
    private final TestExecutor mainThreadExecutor = Mockito.spy(new TestExecutor());

    private final MutableLiveData<List<Project>> projectMutableLiveData = new MutableLiveData<>();

    private CreateTaskViewModel createTaskViewModel;

    @Before
    public void setUp() {
        Mockito.doReturn(projectMutableLiveData).when(repository).getAllProjects();
        Mockito.doReturn("empty_task").when(application).getString(R.string.empty_task);

        projectMutableLiveData.setValue(getDefaultProject());

        createTaskViewModel = new CreateTaskViewModel(application, repository, mainThreadExecutor, ioExecutor);

        Mockito.verify(repository).getAllProjects();
    }

    @Test
    public void nomCase() throws InterruptedException {
        //When
        List<ProjectViewState> projectViewStates = LiveDataTestUtils.getValue(createTaskViewModel.getListProjectViewState());
        String toastMessage = LiveDataTestUtils.getValue(createTaskViewModel.getToastMessageSingleLiveEvent());
        Boolean dismissDialog = LiveDataTestUtils.getValue(createTaskViewModel.getDismissSingleLiveEvent());

        //Then
        assertEquals(getDefaultProjectViewStates(), projectViewStates);
        assertNull(toastMessage);
        assertNull(dismissDialog);
        Mockito.verifyNoMoreInteractions(application, repository, mainThreadExecutor, ioExecutor);
    }

    @Test
    public void emptyProject() throws InterruptedException {
        //Given
        projectMutableLiveData.setValue(new ArrayList<>());

        //When
        List<ProjectViewState> projectViewState = LiveDataTestUtils.getValue(createTaskViewModel.getListProjectViewState());
        String toastMessage = LiveDataTestUtils.getValue(createTaskViewModel.getToastMessageSingleLiveEvent());
        Boolean dismissDialog = LiveDataTestUtils.getValue(createTaskViewModel.getDismissSingleLiveEvent());

        //Then
        assertEquals(new ArrayList<>(), projectViewState);
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
        Boolean dismissDialog = LiveDataTestUtils.getValue(createTaskViewModel.getDismissSingleLiveEvent());

        //Then
        assertNull(projectViewState);
        assertNull(toastMessage);
        assertNull(dismissDialog);
    }

    @Test
    public void addTaskSuccessfully() throws InterruptedException {
        //Given
        String taskDescription = "this is a taskDescription";

        //When
        createTaskViewModel.onOkButtonClicked(2, taskDescription);

        List<ProjectViewState> projectViewStates = LiveDataTestUtils.getValue(createTaskViewModel.getListProjectViewState());
        String toastMessage = LiveDataTestUtils.getValue(createTaskViewModel.getToastMessageSingleLiveEvent());
        Boolean dismissDialog = LiveDataTestUtils.getValue(createTaskViewModel.getDismissSingleLiveEvent());

        //Then
        assertEquals(getDefaultProjectViewStates(), projectViewStates);
        assertNull(toastMessage);
        assertTrue(dismissDialog);
        Mockito.verify(ioExecutor).execute(any());
        Mockito.verify(repository).insertTask(new Task(0, 2, taskDescription));
        Mockito.verify(mainThreadExecutor).execute(any());
        Mockito.verifyNoMoreInteractions(application, repository, ioExecutor, mainThreadExecutor);
    }

    @Test
    public void addTaskFailedBecauseNoDescription() throws InterruptedException {
        //Given
        String taskDescription = "";

        //When
        createTaskViewModel.onOkButtonClicked(2, taskDescription);

        String toastMessage = LiveDataTestUtils.getValue(createTaskViewModel.getToastMessageSingleLiveEvent());
        Boolean dismissDialog = LiveDataTestUtils.getValue(createTaskViewModel.getDismissSingleLiveEvent());

        //Then
        assertEquals("empty_task", toastMessage);
        assertNull(dismissDialog);
        Mockito.verify(application).getString(R.string.empty_task);
        Mockito.verifyNoMoreInteractions(application, repository, mainThreadExecutor, ioExecutor); //ERREUR ICI QUAND DECOMMENTE
    }


    @NonNull
    private List<Project> getDefaultProject() {
        List<Project> projects = new ArrayList<>();
        projects.add(new Project(1, "Projet Tartampion", 0xFFEADAD1));
        projects.add(new Project(2, "Projet Lucidia", 0xFFB4CDBA));
        projects.add(new Project(3, "Projet Circus", 0xFFA3CED2));
        return projects;
    }

    @NonNull
    private List<ProjectViewState> getDefaultProjectViewStates() {
        List<ProjectViewState> projectViewStates = new ArrayList<>();
        projectViewStates.add(new ProjectViewState(1, "Projet Tartampion", 0xFFEADAD1));
        projectViewStates.add(new ProjectViewState(2, "Projet Lucidia", 0xFFB4CDBA));
        projectViewStates.add(new ProjectViewState(3, "Projet Circus", 0xFFA3CED2));
        return projectViewStates;
    }


    public static class RuntimeExceptionAnswer implements Answer<Object> {
        public Object answer( InvocationOnMock invocation ) throws Throwable {
            throw new RuntimeException (invocation.getMethod().getName() + ":" + Arrays.toString(invocation.getArguments()) + " is not mocked" );
        }
    }
}
