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

    @Query("select * from Post WHERE isDeleted == 0")
    List<Post> getAll();

    @Query("select * from Post WHERE content LIKE '%' || :query || '%' AND isDeleted == 0")
    List<Post> getByQuery(String query);

    @Query("select * from Post where userId = :userId")
    List<Post> getPostsByUserId(String userId);

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
