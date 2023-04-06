package com.carelon.whe.data

import android.util.Log
import com.carelon.whe.constants.Constants
import com.carelon.whe.domain.repository.GoogleFitRepository
import com.carelon.whe.util.Logger
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.HistoryClient
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GoogleFitRepositoryImpl @Inject constructor(
    private val historyClient: HistoryClient
) : GoogleFitRepository {

    override suspend fun getAverageStepCount(): Int {

        val endDate = LocalDateTime.now().atZone(ZoneId.systemDefault())
        val startDate = endDate.minusDays(30)

        val readRequest = DataReadRequest.Builder()
            .read(DataType.TYPE_STEP_COUNT_DELTA)
            .setTimeRange(startDate.toEpochSecond(), endDate.toEpochSecond(), TimeUnit.MILLISECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()

        var totalSteps = Constants.AVERAGE_STEP_COUNT
        var bucketCount = 0

        historyClient.readData(readRequest).addOnSuccessListener { response ->
            response.buckets.forEach { bucket ->
                bucket.dataSets.forEach { dataSet ->
                    if (dataSet.dataPoints.isNotEmpty()) {
                        dataSet.dataPoints.forEach { dataPoint ->
                            totalSteps += dataPoint.getValue(Field.FIELD_STEPS).asInt()
                        }
                        bucketCount++
                    }
                }
            }

            if (bucketCount > 0) {
                totalSteps /= bucketCount
            }
        }
            .addOnFailureListener { e ->
                Logger.logD(message = "There was an error reading data from Google Fit")
            }

        return totalSteps
    }
}