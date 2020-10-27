package com.example.pagingdemo;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Student.class}, version = 1, exportSchema = false)
public abstract class StudentsDataBase extends RoomDatabase {

    private static StudentsDataBase instance;

    static synchronized StudentsDataBase getInstance(Context context) {
        if (null == instance) {
            instance = Room.databaseBuilder(context, StudentsDataBase.class, "students_database")
                    .build();
        }
        return instance;
    }

    abstract StudentDao getStudentDao();
}
