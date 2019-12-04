package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
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
