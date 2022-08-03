package com.cmpt362.regathering.database

import com.google.firebase.Timestamp

data class Event(
    var name: String = "",
    var description: String = "",
    var address: String = "",
    var date: Timestamp? = null
)
