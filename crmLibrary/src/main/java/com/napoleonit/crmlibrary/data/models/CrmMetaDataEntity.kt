package com.napoleonit.crmlibrary.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crm_meta_data")
data class CrmMetaDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "device_id") val deviceID: String,
    @ColumnInfo(name = "os_version") val osVersion: String,
    @ColumnInfo(name = "device_locale") val deviceLocale: String,
    @ColumnInfo(name = "app_version_name") val appVersionName: String,
    @ColumnInfo(name = "app_package_name") val appPackageName: String,
    @ColumnInfo(name = "os_name") val osName: String
)
