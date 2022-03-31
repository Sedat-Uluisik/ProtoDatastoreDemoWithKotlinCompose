package com.sedat.protodatastoredemowithkotlincompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sedat.protodatastoredemowithkotlincompose.model.SettingsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProtoViewModel @Inject constructor(
    private val repository: SettingsRepository
): ViewModel(){

    fun saveData(themeColor: String, number: Int, isSaved: Boolean) = viewModelScope.launch{
        repository.saveData(themeColor, number, isSaved)
    }

    fun readData(): Flow<SettingsModel>{
        return repository.readData()
    }
}