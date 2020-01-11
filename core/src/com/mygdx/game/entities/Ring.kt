package com.mygdx.game.entities

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.mapSize

class Ring(camera: OrthographicCamera) {
    private val batch = SpriteBatch()

    init {
        batch.projectionMatrix = camera.combined
    }

    private val texture = Texture("bkg.png")
    private val sprite = Sprite(texture)


    fun draw(camera: OrthographicCamera) {
        val x = -mapSize/2f - camera.position.x
        val y = -mapSize/2f - camera.position.y
        batch.apply {
            begin()
            draw(sprite,x, y, mapSize, mapSize)
            end()
        }
    }

    fun dispose() {
        batch.dispose()
    }
}