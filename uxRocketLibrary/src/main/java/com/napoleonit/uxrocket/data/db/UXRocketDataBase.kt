package com.napoleonit.uxrocket.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.napoleonit.uxrocket.data.db.dao.UXRocketDao
import com.napoleonit.uxrocket.data.models.entity.UXRocketMetaDataEntity

@Database(
    entities = [
        UXRocketMetaDataEntity::class
    ], version = 1, exportSchema = false
)
abstract class UXRocketDataBase : RoomDatabase() {
    abstract fun crmDao(): UXRocketDao
}