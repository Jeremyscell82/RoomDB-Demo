package com.jldemos.roomdb_demo.Utils

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


//Using the same model that the app uses to store the objects, as the object the database uses to store entries
@Entity
data class SuggestionModel(
    @PrimaryKey(autoGenerate = true)
    val dbKey: Int,
    @ColumnInfo(name = "suggestion")
    val suggestion: String,
    @ColumnInfo(name = "accessibility")
    val accessibility: Double,
    @ColumnInfo(name = "type")
    val type: String, //Cannot use enums in ROOM DB
    @ColumnInfo(name = "participants")
    val participants: Int,
    @ColumnInfo(name = "link")
    val link: String,
    @ColumnInfo(name = "key")
    val apiKey: String
)
