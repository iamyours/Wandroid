package io.github.iamyours.wandroid.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.iamyours.wandroid.vo.xxmh.XBook

@Dao
interface XBookDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBook(item: XBook)

    @Query("update XBook set lastReadTime=:lastReadTime,chapterIndex=:chapterIndex where id=:id")
    fun updateBook(lastReadTime: Long, chapterIndex: Int, id: Int)

    @Query("select * from XBook order by lastReadTime desc")
    fun myBookList(): LiveData<List<XBook>>

    @Query("select chapterIndex from XBook where id=:id")
    fun findChapterIndex(id: Int): Int
}