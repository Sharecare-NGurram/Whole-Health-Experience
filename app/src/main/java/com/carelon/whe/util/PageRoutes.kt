package com.carelon.whe.util

sealed class PageRoutes {
    object AppFeatures : PageRoutes()
    data class EarlyAccess(val consent: String) : PageRoutes()
    data class PrivacyPolicy(val consent: String) : PageRoutes()
    object Dashboard : PageRoutes()
    object SignupOption : PageRoutes()
}
