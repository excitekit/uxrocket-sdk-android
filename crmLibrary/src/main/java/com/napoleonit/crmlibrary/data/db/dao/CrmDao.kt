package com.napoleonit.crmlibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.napoleonit.crmlibrary.data.models.CrmMetaDataEntity

@Dao
interface CrmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CrmMetaDataEntity)

    @Query("DELETE FROM crm_meta_data")
    suspend fun delete()

    @Query("SELECT * FROM crm_meta_data")
    suspend fun getEntity(): List<CrmMetaDataEntity>
}