package com.example.appnote.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class NoteRealmDb(
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
    var state: Boolean? = false,
    var userEmail: String? = null,
    var notificationOn: Boolean? = false,
    var alarmOn: Boolean? = false,
    var firebaseID : String? = null
): RealmObject()