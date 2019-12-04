package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.mygdx.game.entities.Player
import com.mygdx.game.entities.Ring
import com.mygdx.game.screens.PlayScreen


class Game : ApplicationAdapter() {
    private lateinit var playScreen: PlayScreen

    override fun create() {
        // now we can create all underlying entities here without lateinit fuckup (Gdx is already loaded)
        // sending 'this' to have access for Game methods in screens (eg setScreen)
        playScreen = PlayScreen(this)
    }

    override fun render() {
        playScreen.render(Gdx.graphics.deltaTime)
    }

    override fun dispose() {
        playScreen.dispose()
    }
}
