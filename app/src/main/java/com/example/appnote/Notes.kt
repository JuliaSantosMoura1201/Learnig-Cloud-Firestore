package com.example.appnote

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Notes(
    @PrimaryKey
    var id: Int? = null,
    var title: String? = null,
    var description:String?=null,
    var day: String? = null,
    var month: String?= null,
    var year: String? = null,
    var hour: String? = null,
    var minute: String? = null,
    var place: String? = null,
    var image: String? = null
): RealmObject()