package com.napoleonit.uxrocket.shared

sealed class UXRocketServer {
    abstract val serverUrl: String

    class develop : UXRocketServer() {
        override val serverUrl: String = "https://apidev.uxrocket.ru/"
    }

    class production : UXRocketServer() {
        override val serverUrl: String = "https://api.uxrocket.ru/"
    }

    class custom(override val serverUrl: String) : UXRocketServer()
}