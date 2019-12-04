package com.mygdx.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2D
import com.badlogic.gdx.physics.box2d.World
import com.mygdx.game.Game
import com.mygdx.game.cameraScale
import com.mygdx.game.entities.Player
import com.mygdx.game.entities.Ring
import com.mygdx.game.targetFPS

class PlayScreen(private val game: Game) : ScreenAdapter() {

    init {
        Box2D.init()
    }

    private val world = World(Vector2(0f, 0f), true)
    private val camera = OrthographicCamera(Gdx.graphics.width.toFloat() / cameraScale, Gdx.graphics.height.toFloat() / cameraScale)

    private val player = Player(world, camera, 0f, 0f)
    private val ring = Ring(camera)

    private fun clear() {
        Gdx.gl.glClearColor(0.16f, 0.16f, 0.16f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    private fun update(delta: Float) {
        player.update(delta)
        camera.position.set(Vector2(player.body.position.x /1.5f, player.body.position.y /1.5f), camera.position.z)
        camera.update()
    }

    private fun draw() {
        ring.draw()
        player.draw()
    }

    override fun render(delta: Float) {
        clear()
        update(delta)
        draw()
        world.step(1 / targetFPS, 6, 2)
    }

    override fun dispose() {
        ring.dispose()
        player.dispose()
    }
}