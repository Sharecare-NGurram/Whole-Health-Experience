package com.carelon.whe.ui.medicine_reminder.scheduler

import com.carelon.whe.domain.model.MedicineReminder

interface AlarmScheduler {
    fun scheduleAlarm(medicineReminder: MedicineReminder)
    fun cancelAlarm(medicineReminder: MedicineReminder)
}