package com.sedat.protodatastoredemowithkotlincompose

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.sedat.protodatastoredemowithkotlincompose.model.SettingsModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class SettingsRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private val Context.datastore: DataStore<Settings> by dataStore(
        fileName = "settings.pb",
        serializer = SettingsSerializer
    )

    suspend fun saveData(themeColor: String, number: Int, isSaved: Boolean) = context.datastore.updateData {
        it.toBuilder()
            .setColor(themeColor)
            .setNumber(number)
            .setIsSaved(isSaved)
            .build()
    }

    fun readData(): Flow<SettingsModel>{
        return context.datastore.data.catch { ex->
            if(ex is IOException){
                emit(Settings.getDefaultInstance())
            }else{
                throw ex
            }
        }.map {
            val settingsModel = SettingsModel(it.color, it.number, it.isSaved)
            settingsModel
        }
    }
}