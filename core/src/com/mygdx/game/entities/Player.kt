package com.mygdx.game.entities

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.mygdx.game.forwardForce
import com.mygdx.game.playerHeight
import com.mygdx.game.playerWidth
import com.mygdx.game.sideForce
import kotlin.math.PI
import com.badlogic.gdx.utils.Array
import com.mygdx.game.utils.Directions

const val ANIMATION_FRAMES_COUNT = 8

class Player(world: World, camera: OrthographicCamera, var x: Float, var y: Float) {
    // Creating the box which represents the fighters' behavior
    // ...inside of the fighter

    private val batch = SpriteBatch()
//    private val debugRenderer = Box2DDebugRenderer(true, false, false, false, false, false)
    private var stateTime = 0f

    val body: Body
    private val rectDef = BodyDef()
    private val rectShape = PolygonShape()
    private val rectFixDef = FixtureDef()

    private val texture = Texture("player.png")
    private val split = TextureRegion.split(texture, texture.width / ANIMATION_FRAMES_COUNT, texture.height)

    // todo there could be MORE kotlin-like creation))))
    private val frames = Array<TextureRegion>().apply {
        for (i in 0 until ANIMATION_FRAMES_COUNT-1) add(split[0][i])
    }

    private val animation = Animation(0.1f, frames)

    init {
        batch.projectionMatrix = camera.combined

        rectDef.apply {
            rectDef.type = BodyDef.BodyType.DynamicBody
            rectDef.position.set(x, y)

            // These lines make fighters slow down and stop. Just like a friction
            rectDef.angularDamping = 25f
            rectDef.linearDamping = 20f
        }

        body = world.createBody(rectDef)

        rectShape.apply {
            setAsBox(playerWidth, playerHeight)
        }

        rectFixDef.apply {
            shape = rectShape
            density = 1f
            restitution = 0.3f
            friction = 1f
        }

        body.createFixture(rectFixDef)

        rectShape.dispose()
    }

    fun pushBody(direction: Directions) {
        val impulseRaw = when(direction) {
            Directions.BOTTOM_LEFT -> Vector2(-sideForce, forwardForce)
            Directions.BOTTOM_RIGHT -> Vector2(sideForce, forwardForce)
            Directions.TOP_LEFT -> Vector2(-sideForce, -forwardForce)
            Directions.TOP_RIGHT -> Vector2(sideForce, -forwardForce)
            Directions.CENTER -> return
        }

        val pointRaw = when(direction) {
            Directions.BOTTOM_LEFT -> Vector2(body.position.x - playerWidth, body.position.y - playerHeight)
            Directions.BOTTOM_RIGHT -> Vector2(body.position.x + playerWidth, body.position.y - playerHeight)
            Directions.TOP_LEFT -> Vector2(body.position.x - playerWidth, body.position.y + playerHeight)
            Directions.TOP_RIGHT -> Vector2(body.position.x + playerWidth, body.position.y + playerHeight)
            Directions.CENTER -> return
        }

        body.applyLinearImpulse(impulseRaw.rotateRad(body.angle), pointRaw.rotateAroundRad(body.position, body.angle), true)
    }

    fun update(delta: Float) {
        stateTime += delta
    }

    fun draw() {
        var frame = animation.getKeyFrame(stateTime, true)

        if (
                body.linearVelocity.len() < 2f &&
                body.angularVelocity < 1f &&
                body.angularVelocity > -1f
        ) {
            frame = animation.getKeyFrame(0f)
        }

        batch.apply {
            begin()
            draw(frame, body.position.x -2f, body.position.y-2f, 2f, 2f, 4f, 4f, 1f, 1f, body.angle / PI.toFloat() * 180f)
            end()
        }
    }

//    fun drawDebug() {
//        debugRenderer.apply {
//            begin()
//            // debuggin here!
//            end()
//        }
//    }

    fun dispose() {
        texture.dispose()
    }

}