package com.example.dolphinlive.Database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dolphinlive.DAO.AssessmentDAO;
import com.example.dolphinlive.DAO.CourseDAO;
import com.example.dolphinlive.DAO.TermDAO;
import com.example.dolphinlive.Entity.Assessment;
import com.example.dolphinlive.Entity.Course;
import com.example.dolphinlive.Entity.Term;

@Database(entities={Term.class, Assessment.class, Course.class}, version=8, exportSchema = false)


public abstract class DatabaseBuilder extends RoomDatabase {
    public abstract TermDAO termDAO();
    public abstract AssessmentDAO assessmentDAO();

    public abstract CourseDAO courseDAO();
    private static volatile DatabaseBuilder INSTANCE;

    static DatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseBuilder.class, "C196Database.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}