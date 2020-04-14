package com.tin.popularmovies.api.models

import com.tin.popularmovies.Const.BASE_IMAGE_MEDIUM
import com.tin.popularmovies.Const.BASE_IMAGE_SMALL

data class Credit(
    val id: Int,
    val cast: List<Cast> = emptyList(),
    val crew: List<Crew> = emptyList()
)

data class Cast(
    val id: Int = -1,
    val cast_id: Int = -1,
    val character: String = "",
    val credit_id: String = "",
    val gender: Int = -1,
    val name: String = "",
    val order: Int = -1,
    val profile_path: String = ""
)

data class Crew(
    val id: Int = -1,
    val credit_id: String = "",
    val name: String = "",
    val job: String = "",
    val profile_path: String = "",
    val department: String = "",
    val gender: Int = -1
)

fun Cast.returnCleanCast() =
    Cast(
        id = id,
        cast_id = cast_id,
        character = character,
        credit_id = credit_id,
        gender = gender,
        name = name,
        order = order,
        profile_path = BASE_IMAGE_SMALL + profile_path
    )
