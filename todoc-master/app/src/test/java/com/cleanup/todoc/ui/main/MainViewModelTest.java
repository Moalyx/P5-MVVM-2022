package com.cleanup.todoc.ui.main;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.Repository;
import com.cleanup.todoc.data.entity.Project;
import com.cleanup.todoc.data.entity.Task;
import com.cleanup.todoc.ui.create_task.ProjectViewState;
import com.cleanup.todoc.utils.LiveDataTestUtils;
import com.cleanup.todoc.utils.TestExecutor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTest {


    private static final Project project1 = new Project(1, "Tartampion", 0xFFEADAD1);
    private static final Project project2 = new Project(2, "Projet Lucidia", 0xFFB4CDBA);

    private static final Task taskForProject1 = new Task(1, project1.getId(), "première tache");
    private static final Task taskForProject2 = new Task(2, project2.getId(), "deuxième tache");

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final Repository repository = Mockito.mock(Repository.class);
    private final Executor ioExecutor = Mockito.spy(new TestExecutor());

    private final MutableLiveData<List<Project>> projectsMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Task>> tasksMutableLiveData = new MutableLiveData<>();

    private MainViewModel mainViewModel;

    @Before
    public void setUp() {
        Mockito.doReturn(projectsMutableLiveData).when(repository).getAllProjects();
        Mockito.doReturn(tasksMutableLiveData).when(repository).getAllTask();

        List<Project> projects = new ArrayList<>();
        projects.add(project1);
        projects.add(project2);
        projectsMutableLiveData.setValue(projects);

        List<Task> tasks = new ArrayList<>();
        tasks.add(taskForProject1);
        tasks.add(taskForProject2);
        tasksMutableLiveData.setValue(tasks);

        mainViewModel = new MainViewModel(repository,ioExecutor);

        Mockito.verify(repository).getAllProjects();
        Mockito.verify(repository).getAllTask();
    }

    @Test
    public void nominalCase() throws InterruptedException {
        //When
//        LiveData<List<TaskViewState>> taskViewLiveData = new MutableLiveData<>();
//        Mockito.doReturn(taskViewLiveData).when(mainViewModel).getViewStateLiveData();
        List<TaskViewState> taskViewStates = LiveDataTestUtils.getValue(mainViewModel.getViewStateLiveData());

        //LiveData<List<TaskViewState>> result = mainViewModel.getViewStateLiveData();

        //Then
        assertEquals(taskViewStates, getDefaultTaskViewStates());
        //Mockito.verify(mainViewModel).getViewStateLiveData();
        Mockito.verify(ioExecutor, Mockito.never()).execute(any());
        Mockito.verifyNoMoreInteractions(repository, ioExecutor);

    }

    @Test
    public void initialCase() throws InterruptedException {
        projectsMutableLiveData.setValue(new ArrayList<>());
        tasksMutableLiveData.setValue(new ArrayList<>());

        List<TaskViewState> taskViewStates = LiveDataTestUtils.getValue(mainViewModel.getViewStateLiveData());

        assertEquals(taskViewStates, getEmptyTaskViewState());
    }

    @Test
    public void sortByAlphabetical() throws InterruptedException {
        mainViewModel.onSortMethodChanged(MainViewModel.SortMethod.NONE);

        List<TaskViewState> taskViewStates = LiveDataTestUtils.getValue(mainViewModel.getViewStateLiveData());

        assertEquals(getDefaultTaskViewStates(), taskViewStates);
    }

    @Test
    public void verifyOnDeleteButtonClicked(){
        //Given
        long taskId = 2;

        //When
        mainViewModel.deleteTask(taskId);

        //Then
        Mockito.verify(repository).deleteTaskById(taskId);
        Mockito.verify(ioExecutor).execute(any());
        Mockito.verifyNoMoreInteractions(repository, ioExecutor);
    }

    @NonNull
    private List<TaskViewState> getDefaultTaskViewStates() {
        List<Project> projects = new ArrayList<>();
        projects.add(project1);
        projects.add(project2);

        List<Task> tasks = new ArrayList<>();
        tasks.add(taskForProject1);
        tasks.add(taskForProject2);

        List<TaskViewState> taskViewStates = new ArrayList<>();

        for (Project project : projects) {
            for (Task task : tasks) {
                if (task.getProjectId() == project.getId()) {
                    taskViewStates.add(
                            new TaskViewState(
                                    task.getId(),
                                    task.getName(),
                                    project.getName(),
                                    project.getColor()
                            )
                    );
                }
            }
        }

        return taskViewStates;
    }

    private List<TaskViewState> getEmptyTaskViewState(){
        return new ArrayList<>();
    }


}
