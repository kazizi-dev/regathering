package com.cmpt362.regathering.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * MyViewModel class is used to store some MutableLiveData
 * for convenient update of necessary variables
 */
class MyViewModel: ViewModel() {
    val userImage = MutableLiveData<Bitmap>()
    val eventImage = MutableLiveData<Bitmap>()
    val userName = MutableLiveData<String>()
    val userEmail = MutableLiveData<String>()
    val userPhone = MutableLiveData<String>()
    val userGender = MutableLiveData<String>()
    val userClass = MutableLiveData<String>()
    val userMajor = MutableLiveData<String>()


    var SEARCHED_EVENTS_COMPUTER_SCIENCE = arrayOf(
                        "Computer Science Git Workshop\n888 University Drive, CSIL Lab Room 2074" +
                            "\n2022-08-24 12:00")

    var SEARCHED_EVENTS_MEETUP = arrayOf("")
}