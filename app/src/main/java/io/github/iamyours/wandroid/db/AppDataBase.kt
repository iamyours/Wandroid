package io.github.iamyours.wandroid.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.iamyours.wandroid.vo.ArticleVO
import io.github.iamyours.wandroid.vo.HistoryArticleVO

@Database(entities = [ArticleVO::class, HistoryArticleVO::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        private var instance: AppDataBase? = null

        fun init(context: Context) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "database"
            )
                .allowMainThreadQueries()
                .build()
        }

        fun get(): AppDataBase {
            return instance!!
        }
    }
}