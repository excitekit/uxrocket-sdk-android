package com.napoleonit.uxrocket.data.sessionCaching


interface IMetaInfo {
    val authKey: String
    val appRocketId: String
    val osName: String
    val deviceID: String
    val osVersion: String
    val deviceLocale: String
    val appVersionName: String
    val appPackageName: String
    val deviceModelName: String
    val deviceType: String
    val resolution: String

    var country: String?
    var city: String?

    fun setCountryAndCity(country: String, city: String)
}