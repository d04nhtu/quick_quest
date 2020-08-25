package com.yeahush.quickquest.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey @ColumnInfo(name = "id") val categoryId: String,
    val name: String,
    val image: String
)