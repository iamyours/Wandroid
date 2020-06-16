package io.github.iamyours.wandroid.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.iamyours.wandroid.vo.ArticleVO
import io.github.iamyours.wandroid.vo.CacheArticleVO
import io.github.iamyours.wandroid.vo.HistoryArticleVO

@Database(
    entities = [ArticleVO::class, HistoryArticleVO::class, CacheArticleVO::class],
    version = 2
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun cacheDao(): CacheDao


    companion object {
        private var instance: AppDataBase? = null

        fun init(context: Context) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "database"
            )
                .addMigrations(MIGRATION_1_2)
                .allowMainThreadQueries()
                .build()
        }

        fun get(): AppDataBase {
            return instance!!
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("create table CacheArticleVO(link text primary key,title text,timestamp integer)")
            }
        }
    }
}