package com.anthonyangatia.mobilemoneyanalyzer

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {

    private val APP_PREF = "com.anthonyangatia.mobilemoneyanalyzer.APP_PREF"
    private val INDEX = "com.anthonyangatia.mobilemoneyanalyzer.INDEX_OF_LIST"
    private val NEW_PHONE = "com.anthonyangatia.mobilemoneyanalyzer.NEW_PHONE"

    private val preferenceObject: SharedPreferences = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
    private val preferenceIndex: SharedPreferences = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)

//    var intExamplePref: Int
//        get() = preferences.getInt(APP_PREF, -1)
//        set(value) = preferences.edit().putInt(APP_PREF, value).apply()

    var objectPref:String?
    get() = preferenceObject.getString(APP_PREF, null) //What is called when trying to retrievee the shared prefference
    set(value) = preferenceObject.edit().putString(APP_PREF, value).apply()

    var index:Int
    get() = preferenceIndex.getInt(INDEX, -1)
    set(value) = preferenceIndex.edit().putInt(INDEX,value).apply()

    var newPhone:Boolean
        get() = preferenceIndex.getBoolean(NEW_PHONE, true)
        set(value) = preferenceIndex.edit().putBoolean(NEW_PHONE,value).apply()



}