package com.nanav.weather.data.model

object MockWeather {
    val JOHN = Character(
        id = 1,
        name = "John",
        description = "Type 1",
        modified = "1234",
        thumbnail = CharacterThumbnail("asas", "jpg")
    )

    val JANE = Character(
        id = 2,
        name = "Jane",
        description = "Type 2",
        modified = "12534",
        thumbnail = CharacterThumbnail("asTas", "jpeg")
    )

    val BOB =Character(
        id = 3,
        name = "Bob",
        description = "Type 3",
        modified = "124534",
        thumbnail = CharacterThumbnail("asATas", "-jpeg")
    )


    val ALL_CHARACTERS = listOf(JOHN, JANE, BOB)

}
