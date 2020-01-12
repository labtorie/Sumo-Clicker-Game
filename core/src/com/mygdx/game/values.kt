package com.mygdx.game

import com.badlogic.gdx.Gdx
import kotlin.math.max
import kotlin.math.min

const val cameraScale  = 80f // We don't need to change it anymore, actually we can express this value with the later
const val sideForce    = 75f
const val forwardForce = 100f
const val playerWidth  = 2f
const val playerHeight = 2f
const val mapSize      = 20f
const val targetFPS    = 60f

// some cool code that makes the ring fully visible on any screen
val actualScale        = (min(Gdx.graphics.height.toFloat(), Gdx.graphics.width.toFloat()) / max(Gdx.graphics.height.toFloat(), Gdx.graphics.width.toFloat())) * cameraScale


