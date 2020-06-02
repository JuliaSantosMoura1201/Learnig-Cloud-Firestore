package com.example.appnote

data class Note(
    var id: String?= null,
    var title: String? = null,
    var description:String?=null,
    var day: String? = null,
    var month: String?= null,
    var year: String? = null,
    var hour: String? = null,
    var minute: String? = null,
    var place: String? = null,
    var state: Boolean? = false
)