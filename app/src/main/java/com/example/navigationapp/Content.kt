package com.example.navigationapp

import androidx.annotation.DrawableRes
val doctor = Content(
    id = 0,
    title = "doctor?",
    thumbnail = R.drawable.doctor,
    body = "Looking for a doctor?"
)
val group = Content(
    id = 1,
    title = "group?",
    thumbnail = R.drawable.group,
    body = "Looking for a group?"
)
val wearables = Content(
    id = 2,
    title = "wearables?",
    thumbnail = R.drawable.wearables,
    body = "Looking for wearables?"
)
val workout = Content(
    id = 3,
    title = "Workout?",
    thumbnail = R.drawable.workout,
    body = "Looking for a place to workout"
)

data class Content(
    val id:Long,
    val title:String,
    @DrawableRes val thumbnail : Int,
    val body : String
)

val allArticles = listOf(doctor, group, wearables, workout)
