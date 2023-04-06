package com.carelon.whe.util

/**
 * Navigation Direction
 */
enum class NavigationDirection {
    BACKWARD,
    FORWARD,
    UPWARD,
    DOWNWARD
}

enum class TackMedicationStatus(val value:Int){
    MEDICATION_IMPORT_OK(1),
    MEDICATION_IMPORT_NOT_NOW(0),
    MEDICATION_NOT_CHOOSE_AS_FOCUS_AREA(-1)
}

enum class TrackYourStepsStatus(val value:Int){
    TRACK_YOUR_STEPS_OK(1),
    TRACK_YOUR_STEPS_DENIED(2),
    TRACK_YOUR_STEPS_NOT_STARTED(0)
}