package com.kwdevs.hospitalsdashboard.app

import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CRUD
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.actionList
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.scopeList
import com.kwdevs.hospitalsdashboard.views.assets.DOT
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.SPACE

fun String.removeCrudPrefix(): String {
    val match = actionList.firstOrNull { this.startsWith(it.third) }
    return if (match != null) this.removePrefix(match.third) else this
}
fun String.getCrudPrefix(): String {
    val match = actionList.firstOrNull { this.startsWith(it.third) }
    return match?.third?.replace(":", EMPTY_STRING) ?: EMPTY_STRING
}
fun String.removeModulePrefix(): String {
    val match = scopeList.firstOrNull { this.startsWith(it.third) }
    return if (match != null) this.removePrefix(match.third) else this
}
fun String.getModulePrefix(): String {
    val match = scopeList.filter { this.startsWith(it.third) }.maxByOrNull { it.third.length }// خد الأطول
    return (match?.third ?: EMPTY_STRING).replace("@", EMPTY_STRING)
}
fun String.replaceSpaceWithDot(): String{ return this.replace(SPACE,DOT) }

fun returnAction(action:CRUD): String {
    return when(action){
        CRUD.BROWSE->"browse:"
        CRUD.CREATE->"create:"
        CRUD.UPDATE->"update:"
        CRUD.DELETE->"delete:"
        CRUD.VIEW->"view:" //with models
        CRUD.READ->"read:"
        CRUD.RESTORE->"restore:"
    }
}
fun returnCrudAction(actionString:String): CRUD? {
    return when(actionString){
        "browse:"->CRUD.BROWSE
        "create:"->CRUD.CREATE
        "update:"->CRUD.UPDATE
        "delete:"->CRUD.DELETE
        "view:"->CRUD.VIEW
        "read:"->CRUD.READ
        "restore:"->CRUD.RESTORE
        else->null
    }
}
