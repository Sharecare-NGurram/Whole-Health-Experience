package com.carelon.whe.ui.dashboard.foryou.calender

import java.util.Date

data class CurrentDayRecord(
    var date : Date,
    var currentSelection : Int,
    var progress:Int,
    var enabled : Int=1,
)
