package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.Array
import kotlin.math.PI


class MyGdxGame : ApplicationAdapter() {
    var world = World(Vector2(0f, 0f), true)
    lateinit var debugRenderer: Box2DDebugRenderer
    lateinit var camera: OrthographicCamera
    lateinit var body: Body
    lateinit var batch: SpriteBatch
    lateinit var texture: Texture
    lateinit var ringTexture: Texture
    lateinit var sprite: Sprite
    lateinit var ringSprite: Sprite
    lateinit var shapeRenderer: ShapeRenderer
    lateinit var anim: Animation<TextureRegion>
    var stateTime = 0f
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
        debugRenderer = Box2DDebugRenderer(true, false, false, false, false, false)
        createPlayer(world, 0f, 0f)

        batch = SpriteBatch()
        texture = Texture("player.png")
        val split = TextureRegion.split(texture, texture.width / 8, texture.height)
      //  val frames = Array<TextureRegion>().apply {
      //      for (i in 0 until size) add(split[0][i])
      //  }

        val frames = Array<TextureRegion>()
        for (i in 0..7)
        {
            frames.add(split[0][i])
        }
        anim = Animation<TextureRegion>(0.1f, frames)

        ringTexture = Texture("bkg.png")

        sprite = Sprite(texture)
        ringSprite = Sprite(ringTexture)

        shapeRenderer = ShapeRenderer()
        stateTime = 0f

    }


    private fun pushBody(dir: Int) {
        lateinit var impulseRaw: Vector2
        lateinit var pointRaw: Vector2
        if (dir == 0) { // Pushing bottom-left corner
            impulseRaw = Vector2(-sideForce, forwardForce) // Creating the impulse in body's coordinates
            pointRaw = Vector2(body.position.x - playerWidth, body.position.y - playerHeight) // Same for the point we want to push
        }

        if (dir == 1) {
            impulseRaw = Vector2(sideForce, forwardForce)
            pointRaw = Vector2(body.position.x + playerWidth, body.position.y - playerHeight)

        }
        if (dir == 2) {
            impulseRaw = Vector2(-sideForce, -forwardForce)
            pointRaw = Vector2(body.position.x - playerWidth, body.position.y + playerHeight)
        }
        if (dir == 3) {
            impulseRaw = Vector2(sideForce, -forwardForce)
            pointRaw = Vector2(body.position.x + playerWidth, body.position.y + playerHeight)
        }

        body.applyLinearImpulse(impulseRaw.rotateRad(body.angle), pointRaw.rotateAroundRad(body.position, body.angle), true)
    }


    override fun render() {
        Gdx.gl.glClearColor(0.16f, 0.16f, 0.16f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            pushBody(0)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            pushBody(1)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            pushBody(2)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            pushBody(3)
        }
           camera.position.set(Vector2(body.position.x /1.5f, body.position.y /1.5f), camera.position.z)

        stateTime += Gdx.graphics.deltaTime

      batch.projectionMatrix = camera.combined
        var frame = anim.getKeyFrame(stateTime, true)
        if (body.linearVelocity.len() < 2f && (body.angularVelocity < 1f && body.angularVelocity > -1f))
        {
            frame = anim.getKeyFrame(0f)
        }


        batch.begin()

        batch.draw(ringSprite,-7f, -7f, 14f, 14f)
        batch.draw(frame, body.position.x -2f, body.position.y-2f, 2f, 2f, 4f, 4f, 1f, 1f, body.angle / PI.toFloat() * 180f)
        batch.end()
        camera.update()
        world.step(1 / targetFPS, 6, 2)
    }

    override fun dispose() {
        batch.dispose()
        texture.dispose()
    }
}
