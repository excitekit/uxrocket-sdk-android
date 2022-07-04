package com.napoleonit.crmlibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.napoleonit.crmlibrary.data.db.dao.UXRocketDao
import com.napoleonit.crmlibrary.data.models.UXRocketMetaDataEntity

@Database(
    entities = [
        UXRocketMetaDataEntity::class
    ], version = 1, exportSchema = false
)
abstract class UXRocketDataBase : RoomDatabase() {
    abstract fun crmDao(): UXRocketDao
}