package com.napoleonit.uxrocket.data.useCases

import com.napoleonit.uxrocket.data.base.Either
import com.napoleonit.uxrocket.data.base.Failure
import com.napoleonit.uxrocket.data.base.Success
import com.napoleonit.uxrocket.data.base.UseCase
import com.napoleonit.uxrocket.data.cache.globalCaching.ITaskCaching
import com.napoleonit.uxrocket.shared.logError
import com.napoleonit.uxrocket.shared.logInfo

class SaveAppParamsFromCacheUseCase(
    private val saveAppParamsUseCase: SaveAppParamsUseCase,
    private val taskCaching: ITaskCaching
) : UseCase<Unit, Unit>() {
    private var isTaskRunning = false
    override suspend fun run(params: Unit): Either<Exception, Unit> {
        if (isTaskRunning) return Failure(Exception())
        else isTaskRunning = true

        val cachingData = taskCaching.logEventTaskList
        if (cachingData.isEmpty()) {
            "Cached SaveRowAppData empty".logInfo()
            return Success(Unit)
        }

        val results = ArrayList<Either<Exception, Unit>>()
        cachingData.forEach { logModel ->
            results.add(saveAppParamsUseCase.run(logModel))
        }


        isTaskRunning = false
        return if (results.find { it is Failure } != null) {
            "Failed to send SaveRowAppData requests from cache".logError()
            Failure(Exception())
        } else {
            "Send requests SaveRowAppData from cache successfully".logError()
            taskCaching.removeLogEventTaskList()
            Success(Unit)
        }

    }
}