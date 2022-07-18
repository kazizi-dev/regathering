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
    val userName = MutableLiveData<String>()
    val userEmail = MutableLiveData<String>()
    val userPhone = MutableLiveData<String>()
    val userGender = MutableLiveData<String>()
    val userClass = MutableLiveData<String>()
    val userMajor = MutableLiveData<String>()
}