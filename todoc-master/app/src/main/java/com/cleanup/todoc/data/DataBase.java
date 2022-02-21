package com.cleanup.todoc.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.data.dao.ProjectDao;
import com.cleanup.todoc.data.dao.TaskDao;
import com.cleanup.todoc.data.entity.Project;
import com.cleanup.todoc.data.entity.Task;

import java.util.concurrent.Executor;

@Database(
        entities = {
                Project.class,
                Task.class
        },
        version = 1,
        exportSchema = false
)
public abstract class DataBase extends RoomDatabase {

    private static final String DATABASE_NAME = "dataBase";

    private static DataBase instance;

    public abstract ProjectDao projectDao();

    public abstract TaskDao taskDao();

    public static DataBase getInstance(
            @NonNull Application application,
            @NonNull Executor executor) {
        if (instance == null) {
            synchronized (DataBase.class) {
                if (instance == null) {
                    instance = create(
                            application,
                            executor
                    );
                }
            }
        }
        return instance;
    }

    private static DataBase create(
            @NonNull Application application,
            @NonNull Executor executor
    ) {
        Builder<DataBase> builder = Room.databaseBuilder(
                application,
                DataBase.class,
                DATABASE_NAME
        );

        builder.addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {

                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        ProjectDao projectDao = DataBase.getInstance(application, executor).projectDao();
                        projectDao.insertProject(new Project(1L, "Projet Tartampion", 0xFFEADAD1));
                        projectDao.insertProject(new Project(2L, "Projet Lucidia", 0xFFB4CDBA));
                        projectDao.insertProject(new Project(3L, "Projet Circus", 0xFFA3CED2));

                    }
                });
            }
        });
        return builder.build();
    }
}
