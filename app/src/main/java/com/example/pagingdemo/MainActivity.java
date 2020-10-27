package com.example.pagingdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button buttonGenerate, buttonClear;
    StudentDao studentDao;
    StudentsDataBase studentsDataBase;
    MyPageAdapter myPageAdapter;
    LiveData<PagedList<Student>> allStudentsLivaPaged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycleView);

        myPageAdapter = new MyPageAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(myPageAdapter);
        studentsDataBase = StudentsDataBase.getInstance(this);
        studentDao = studentsDataBase.getStudentDao();
        allStudentsLivaPaged = new LivePagedListBuilder<>(studentDao.getAllStudents(), 4)
                .build();
        allStudentsLivaPaged.observe(this, new Observer<PagedList<Student>>() {
            @Override
            public void onChanged(PagedList<Student> students) {
                myPageAdapter.submitList(students);
            }
        });
        buttonGenerate = findViewById(R.id.buttonGenerate);
        buttonClear = findViewById(R.id.buttonClear);
        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student[] students = new Student[200];
                for (int i = 0; i < 200; i++) {
                    Student student = new Student();
                    student.setNumber(i);
                    students[i] = student;
                }
                new InsertAsyncTask(studentDao).execute(students);

            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClearAsyncTask(studentDao).execute();
            }
        });
    }

    static class InsertAsyncTask extends AsyncTask<Student, Void, Void> {
        StudentDao studentDao;

        public InsertAsyncTask(StudentDao studentDao) {
            this.studentDao = studentDao;
        }

        @Override
        protected Void doInBackground(Student... students) {
            studentDao.InsertStudents(students);
            return null;
        }
    }

    static class ClearAsyncTask extends AsyncTask<Void, Void, Void> {
        StudentDao studentDao;

        public ClearAsyncTask(StudentDao studentDao) {
            this.studentDao = studentDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            studentDao.DeleteAllStudents();
            return null;
        }
    }


}