package com.cleanup.todoc.ui.create.task;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.Repository;
import com.cleanup.todoc.ui.main.MainViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.Executor;

@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTest {






    @Rule
   public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final Repository repository = Mockito.mock(Repository.class);
    private  final Executor ioExecutor = Mockito.mock(Executor.class);

    private MainViewModel mainViewModel;

    @Before
    public void setUp(){

    }




}
