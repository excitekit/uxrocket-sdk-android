package com.napoleonit.crmlibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.napoleonit.crmlibrary.data.db.dao.CrmDao
import com.napoleonit.crmlibrary.data.models.CrmMetaDataEntity

@Database(
    entities = [
        CrmMetaDataEntity::class
    ], version = 1, exportSchema = false
)
abstract class GetCrmDataBase : RoomDatabase() {
    abstract fun crmDao(): CrmDao
}