package com.carelon.whe.domain.usecase

import com.carelon.whe.constants.Constants
import com.carelon.whe.domain.repository.GoogleFitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAverageStepCountUseCase @Inject constructor(
    private val googleFitRepository: GoogleFitRepository
) {

    suspend fun getAverageStepCount() = flow {
        try {
            emit(googleFitRepository.getAverageStepCount())
        } catch (e: Exception) {
            emit(Constants.AVERAGE_STEP_COUNT)
        }
    }.flowOn(Dispatchers.IO)

}