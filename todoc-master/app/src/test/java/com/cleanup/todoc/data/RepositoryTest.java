package com.cleanup.todoc.data;

import static org.junit.Assert.assertEquals;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.dao.ProjectDao;
import com.cleanup.todoc.data.dao.TaskDao;
import com.cleanup.todoc.data.entity.Project;
import com.cleanup.todoc.data.entity.Task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryTest {

    private final TaskDao taskDao = Mockito.mock(TaskDao.class);
    private final ProjectDao projectDao = Mockito.mock(ProjectDao.class);
    private Repository repository;

    @Before
    public void setUp() {
        repository = new Repository(projectDao, taskDao);
    }

    @Test
    public void verifyGetAllProjects() {

        LiveData<List<Project>> projectsLiveData = new MutableLiveData<>(); // permet de créer une livedata factice
        Mockito.doReturn(projectsLiveData).when(projectDao).getAllProjects(); // on indique que lorsque la methode de l'interface est appelé elle retournera la factice livedata
        //Mockito.when(projectDao.getAllProjects()).thenReturn(projectsLiveData); // autre maniere d'écrire que la ligne precedente


        LiveData<List<Project>> result = repository.getAllProjects(); // on fait appel au dao via le repo

        assertEquals(projectsLiveData, result); // on verifie que le resultat est le meme que l'on passe directement par le dao ou par le repo ce qui signifie que le repo fonctionne
        Mockito.verify(projectDao).getAllProjects(); // verify que la methode a bien était appelé
        Mockito.verifyNoMoreInteractions(projectDao, taskDao);
    }

    @Test
    public void verifyGetAllTasks() {
        LiveData<List<Task>> tasksLiveData = new MutableLiveData<>();
        Mockito.doReturn(tasksLiveData).when(taskDao).getAllTask();

        LiveData<List<Task>> result = repository.getAllTask();

        assertEquals(tasksLiveData, result);
        Mockito.verify(taskDao).getAllTask();
        Mockito.verifyNoMoreInteractions(projectDao, taskDao);
    }

    @Test
    public void verifyInsertTask() {
        Task task = Mockito.mock(Task.class);

        repository.insertTask(task);

        Mockito.verify(taskDao).insertTask(task);
        Mockito.verifyNoMoreInteractions(projectDao, taskDao);
    }

    @Test
    public void verifyDeleteTask() {
        long taskId = 1;

        repository.deleteTaskById(taskId);

        Mockito.verify(taskDao).deleteTaskById(taskId);
        Mockito.verifyNoMoreInteractions(projectDao, taskDao);
    }

}
