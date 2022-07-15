package com.napoleonit.uxrocket.shared

import com.napoleonit.uxrocket.UXRocket
import com.napoleonit.uxrocket.data.exceptions.UXRocketNotInitializedException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.execute(
    block: suspend CoroutineScope.() -> Unit,
    coroutineScope: CoroutineContext = Dispatchers.IO
) = SupervisorJob(launch(coroutineScope) {
    if (!UXRocket.isInitialized) throw UXRocketNotInitializedException()
    else {
        block()
    }
})