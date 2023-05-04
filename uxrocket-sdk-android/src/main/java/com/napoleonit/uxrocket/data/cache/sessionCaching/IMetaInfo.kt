package com.napoleonit.uxrocket.data.cache.sessionCaching


interface IMetaInfo {
    val authKey: String
    val appRocketId: String
    val osName: String
    val deviceID: String
    val osVersion: String
    val deviceLocale: String
    val operatorName: String?
    val appVersionName: String
    val appPackageName: String
    val deviceModelName: String
    val deviceType: String
    val resolution: String
    val visitor: String
    val session: String
    var country: String?
    var city: String?

    fun setCountryAndCity(country: String, city: String)
}