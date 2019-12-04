package com.mygdx.game.entities

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Ring(camera: OrthographicCamera) {
    private val batch = SpriteBatch()

    init {
        batch.projectionMatrix = camera.combined
    }

    private val texture = Texture("bkg.png")
    private val sprite = Sprite(texture)

    fun draw() {
        batch.apply {
            begin()
            draw(sprite,-7f, -7f, 14f, 14f)
            end()
        }
    }

    fun dispose() {
        batch.dispose()
    }
}