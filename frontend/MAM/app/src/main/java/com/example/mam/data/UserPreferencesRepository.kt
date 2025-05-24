package com.example.mam.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
class UserPreferencesRepository (
    private val dataStore: DataStore<Preferences>
){
}