package com.flash.locationsharing_job3.model

import kotlin.time.Duration

data class AppUser(
    val userId: String = "",
    val email: String = "",
    val userName: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
)
