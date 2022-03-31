package com.sedat.protodatastoredemowithkotlincompose.model

data class SettingsModel(
    val color: String = "Red",
    val number: Int = 0,
    val isSaved: Boolean = false
)
