package com.example.datastoreapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "items")
class Item(
    @PrimaryKey
    val name: String,
    val age: Int,
    val quantity: String
)