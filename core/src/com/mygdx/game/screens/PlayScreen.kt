package com.mygdx.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2D
import com.badlogic.gdx.physics.box2d.World
import com.mygdx.game.Game
import com.mygdx.game.actualScale
import com.mygdx.game.entities.Foreground
import com.mygdx.game.entities.Player
import com.mygdx.game.entities.Ring
import com.mygdx.game.targetFPS
import com.mygdx.game.utils.dir_lock
import com.mygdx.game.utils.getDirectionByScreenCoords

class PlayScreen(private val game: Game) : ScreenAdapter() {
    init {
        Box2D.init()
        Gdx.input.inputProcessor = PlayScreenInputAdapter(this)
    }

    private val world = World(Vector2(0f, 0f), true)
    private val camera = OrthographicCamera(
            Gdx.graphics.width.toFloat() / (actualScale * Gdx.graphics.density),
            Gdx.graphics.height.toFloat() / (actualScale * Gdx.graphics.density))

    private val player = Player(world, camera, 0f, 0f)
    private val ring = Ring(camera)
    private val foreground = Foreground(camera)


    private fun clear() {
        Gdx.gl.glClearColor(0.16f, 0.16f, 0.16f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    private fun update(delta: Float) {
        player.update(delta)
        //   camera.position.set(Vector2(player.body.position.x / 1.5f, player.body.position.y / 1.5f), camera.position.z)
        camera.update()
    }

    fun handleTouch(x: Int, y: Int) {
        player.pushBody(getDirectionByScreenCoords(x, y))
    }

    fun handleKeyTyped(char: Char) {
        // the same for keys... but we don't need them actually :)
    }

    private fun draw() {
        ring.draw(camera)
        player.draw()
        foreground.draw(camera)
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
        foreground.dispose()
    }
}

class PlayScreenInputAdapter(private val screen: PlayScreen) : InputAdapter() {
    override fun keyTyped(char: Char): Boolean {
        screen.handleKeyTyped(char)
        return true
    }

    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        if (!dir_lock[getDirectionByScreenCoords(x, y).ordinal]) {
            screen.handleTouch(x, y)
            dir_lock[getDirectionByScreenCoords(x, y).ordinal] = true
        }
        return true
    }

    override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        dir_lock[getDirectionByScreenCoords(x, y).ordinal] = false
        return true
    }
}