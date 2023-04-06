package com.carelon.whe.domain.usecase

import com.carelon.whe.domain.model.MedicineReminder
import com.carelon.whe.domain.repository.MedicineReminderRepository
import com.carelon.whe.network.Resource
import com.carelon.whe.util.ComparatorReminder
import com.carelon.whe.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

/**
 * Use Case for getting all medicine reminders from database
 */
class GetMedicineRemindersUseCase @Inject constructor(private val repository: MedicineReminderRepository) {

    suspend fun getMedicineReminders() = flow {
        try {
            val list = repository.getMedicineReminders()
            Collections.sort(list, ComparatorReminder())
            emit(Resource.Success(list))
        }catch (e: Exception){
            emit(NetworkUtils.resolveError(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getMedicineReminderList(): List<MedicineReminder> {
        var list: List<MedicineReminder>
        coroutineScope {
            list = withContext(Dispatchers.IO) { repository.getMedicineReminders() }
        }
        return list
    }

}