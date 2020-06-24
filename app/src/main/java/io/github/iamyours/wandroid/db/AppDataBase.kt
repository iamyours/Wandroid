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
import io.github.iamyours.wandroid.vo.UrlTypeVO
import io.github.iamyours.wandroid.vo.xxmh.XBook

@Database(
    entities = [ArticleVO::class, HistoryArticleVO::class,
        CacheArticleVO::class, XBook::class, UrlTypeVO::class],
    version = 4
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun cacheDao(): CacheDao
    abstract fun bookDao(): XBookDao
    abstract fun urlTypeDao(): UrlTypeDao


    companion object {
        private var instance: AppDataBase? = null

        fun init(context: Context) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "database"
            )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                .allowMainThreadQueries()
                .build()
        }

        fun get(): AppDataBase {
            return instance!!
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(_db: SupportSQLiteDatabase) {
                _db.execSQL("CREATE TABLE IF NOT EXISTS `CacheArticleVO` (`link` TEXT NOT NULL, `title` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`link`))")
            }
        }
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(_db: SupportSQLiteDatabase) {
                _db.execSQL("CREATE TABLE IF NOT EXISTS `XBook` (`id` INTEGER NOT NULL, `name` TEXT, `author` TEXT, `description` TEXT, `keywords` TEXT, `categoryId` INTEGER NOT NULL, `category` TEXT, `coverUrl` TEXT, `extensionUrl` TEXT, `chapterCount` INTEGER NOT NULL, `score` REAL NOT NULL, PRIMARY KEY(`id`))")
            }
        }
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(_db: SupportSQLiteDatabase) {
                _db.execSQL("CREATE TABLE IF NOT EXISTS `UrlTypeVO` (`urlMd5` TEXT NOT NULL, `type` TEXT NOT NULL, PRIMARY KEY(`urlMd5`))")
            }
        }
    }
}