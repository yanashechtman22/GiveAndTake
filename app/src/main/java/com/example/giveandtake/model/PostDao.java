package com.example.giveandtake.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostDao {

    @Query("select * from Post")
    List<Post> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... students);

    @Insert
    void insert(Post post);

    @Query("DELETE FROM Post WHERE id = :postId")
    void deleteById(String postId);

    @Delete
    void delete(Post post);

    @Update
    void update(Post post);

}
