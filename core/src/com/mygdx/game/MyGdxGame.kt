package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*


class MyGdxGame : ApplicationAdapter() {
    var world = World(Vector2(0f, 0f), true)
    lateinit var debugRenderer: Box2DDebugRenderer
    lateinit var camera: OrthographicCamera
    lateinit var body: Body

    // Creating the box which represents the fighters' behavior
    private fun createPlayer(world: World, x: Float, y: Float) {
        val rectDef = BodyDef()
        rectDef.type = BodyDef.BodyType.DynamicBody
        rectDef.position.set(x, y)

        // These lines make fighters slow down and stop. Just like a friction
        rectDef.angularDamping = 25f
        rectDef.linearDamping = 20f

        val rectShape = PolygonShape()
        rectShape.setAsBox(playerWidth, playerHeight)

        val rectFixDef = FixtureDef()
        rectFixDef.shape = rectShape

        // Guess I don't need these parameters as long as there is one single body
        rectFixDef.density = 1f
        rectFixDef.restitution = 0.3f
        rectFixDef.friction = 1f

        body = world.createBody(rectDef)
        body.createFixture(rectFixDef)
        rectShape.dispose()

    }

    override fun create() {
        Box2D.init()
        camera = OrthographicCamera(Gdx.graphics.width.toFloat() / cameraScale, Gdx.graphics.height.toFloat() / cameraScale)
        debugRenderer = Box2DDebugRenderer(true, true, false, true, true, true)
        createPlayer(world, 0f, 0f)

    }

    // I can't figure out what am I doing wrong. Lemme explain....
    private fun pushBody(dir: Int) {
        lateinit var impulseRaw: Vector2
        lateinit var pointRaw: Vector2
        // Gonna describe the first case
        if (dir == 0) { // Pushing bottom-left corner
            impulseRaw = Vector2(-sideForce, forwardForce) // Creating the impulse in body's coordinates
            pointRaw = Vector2(-playerWidth, -playerHeight) // Same for the point we want to push
        }
        if (dir == 1) {
            impulseRaw = Vector2(sideForce, forwardForce)
            pointRaw = Vector2(playerWidth, -playerHeight)
        }
        if (dir == 2) {
            impulseRaw = Vector2(-sideForce, -forwardForce)
            pointRaw = Vector2(-playerWidth, playerHeight)
        }
        if (dir == 3) {
            impulseRaw = Vector2(sideForce, -forwardForce)
            pointRaw = Vector2(playerWidth, playerHeight)
        }

        // And now we've got a func below that takes impulse vector, point vector and some shitty bool as parameters
        // I use a rotateRad() method to make the force depended on the body's angle
        // As a result it seems like it works properly, but it does not. I be like: FFFFFFUUUUUUUUUUUU!!!!!!!!
        body.applyLinearImpulse(impulseRaw.rotateRad(body.angle), pointRaw.rotateRad(body.angle), true)
    }

    override fun render() {
        Gdx.gl.glClearColor(0.16f, 0.16f, 0.16f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            pushBody(0)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            pushBody(1)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            pushBody(2)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            pushBody(3)
        }
        debugRenderer.render(world, camera.combined)
        world.step(1 / targetFPS, 6, 2)
    }

    override fun dispose() {

    }
}
