package io.github.iamyours.wandroid.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.iamyours.wandroid.vo.ArticleVO
import io.github.iamyours.wandroid.vo.HistoryArticleVO

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addArticle(item: ArticleVO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addHistory(item: HistoryArticleVO)

    @Query("SELECT 1 FROM HistoryArticleVO WHERE username=:username and articleId=:articleId")
    fun isRead(username: String, articleId: Int): Boolean

    @Query("SELECT a.* FROM HistoryArticleVO h LEFT JOIN ARTICLEVO a ON h.articleId=a.id WHERE h.username=:username ORDER BY h.timestamp desc limit :size offset :offset")
    fun historyList(
        username: String,
        offset: Int,
        size: Int
    ): LiveData<List<ArticleVO>>
}