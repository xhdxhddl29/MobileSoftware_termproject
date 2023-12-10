package com.example.yumup.data;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.yumup.data.DietEntity;

import java.util.List;

@Dao
public interface DietDao {
    @Query("SELECT * FROM diet")
    List<DietEntity> getAll();

    @Query("SELECT * FROM diet WHERE diet.mealDateTime like '%' || :yearMonth || '%' ")
    List<DietEntity> getDietsByMonth(String yearMonth);

    @Query("DELETE FROM diet WHERE diet.id = :id")
    void delete(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DietEntity dietEntity);
}