package com.mygdx.game.utils

import com.badlogic.gdx.Gdx

enum class Directions {
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    TOP_LEFT,
    TOP_RIGHT,
    CENTER,
}

fun getDirectionByScreenCoords(xCoord: Int, yCoord: Int): Directions {
    // translate to school coordinate system :)
    val x = xCoord - Gdx.graphics.width/2
    val y = - yCoord + Gdx.graphics.height/2
    return when {
        x < 0 && y < 0 -> Directions.BOTTOM_LEFT
        x < 0 && y > 0 -> Directions.TOP_LEFT
        x > 0 && y > 0 -> Directions.TOP_RIGHT
        x > 0 && y < 0 -> Directions.BOTTOM_RIGHT
        else -> Directions.CENTER
    }
}