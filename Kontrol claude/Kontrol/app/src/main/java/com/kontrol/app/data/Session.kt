package com.kontrol.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.store by preferencesDataStore("kontrol_session")
class SessionManager(private val context:Context){
 private val tokenKey=stringPreferencesKey("token")
 suspend fun token():String?=context.store.data.first()[tokenKey]
 suspend fun save(token:String){context.store.edit{it[tokenKey]=token}}
 suspend fun clear(){context.store.edit{it.remove(tokenKey)}}
}
