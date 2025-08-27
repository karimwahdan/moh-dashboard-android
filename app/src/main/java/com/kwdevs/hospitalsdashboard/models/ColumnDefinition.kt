package com.kwdevs.hospitalsdashboard.models

data class ColumnDefinition<T>(
    val key: String,
    val header: String,
    val cellText: (T) -> String,
    val weight:Float=1f,
    val editable: Boolean = false,
    val required: Boolean = false,
    val onSave: ((rowId: String, updatedFields: Map<String, String>) -> Unit)? = null
)