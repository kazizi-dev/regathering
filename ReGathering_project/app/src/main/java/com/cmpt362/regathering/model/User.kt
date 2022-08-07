package com.cmpt362.regathering.model

import android.app.Notification

data class User (
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var interests: ArrayList<String> = ArrayList(),
    var joinedEvents: ArrayList<String> = ArrayList(),
    var suggestedEvents: ArrayList<String> = ArrayList(),
    var notifications: ArrayList<String> = ArrayList(),
    var hostedEvents: ArrayList<String> = ArrayList(),
    var userId: String = ""
)