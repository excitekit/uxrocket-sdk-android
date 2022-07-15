package com.napoleonit.uxrocket.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.napoleonit.uxrocket.data.models.entity.UXRocketMetaDataEntity

@Dao
interface UXRocketDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UXRocketMetaDataEntity)

    @Query("DELETE FROM crm_meta_data")
    suspend fun delete()

    @Query("SELECT * FROM crm_meta_data")
    suspend fun getEntity(): List<UXRocketMetaDataEntity>
}