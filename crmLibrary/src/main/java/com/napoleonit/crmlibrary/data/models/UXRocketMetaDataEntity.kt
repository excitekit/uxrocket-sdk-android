package com.napoleonit.crmlibrary.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crm_meta_data")
data class UXRocketMetaDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "auth_key") val authKey: String,
    @ColumnInfo(name = "device_id") val deviceID: String,
    @ColumnInfo(name = "os_version") val osVersion: String,
    @ColumnInfo(name = "device_locale") val deviceLocale: String,
    @ColumnInfo(name = "app_version_name") val appVersionName: String,
    @ColumnInfo(name = "app_package_name") val appPackageName: String,
    @ColumnInfo(name = "device_model_name") val deviceModelName: String,
    @ColumnInfo(name = "os_name") val osName: String,
    @ColumnInfo(name = "app_rocket_id") val appRocketId: String,
    @ColumnInfo(name = "device_type") val deviceType: String
)
