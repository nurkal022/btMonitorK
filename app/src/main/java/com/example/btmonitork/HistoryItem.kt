package com.example.btmonitork

import java.io.Serializable

data class HistoryItem(
    var id: String,
    var date: String,
    var data: String,
    var prediction: String
): Serializable
