package com.example.dolphinlive.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dolphinlive.Entity.Term;

import java.util.List;

@Dao
public interface TermDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Term term);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);


    @Query("SELECT * FROM Terms")
    List<Term> getAllTerms();

}