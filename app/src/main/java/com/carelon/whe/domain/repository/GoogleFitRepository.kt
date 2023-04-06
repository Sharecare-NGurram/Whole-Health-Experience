package com.carelon.whe.domain.repository

interface GoogleFitRepository {

    suspend fun getAverageStepCount(): Int

}