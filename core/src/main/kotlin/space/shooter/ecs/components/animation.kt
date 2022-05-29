package space.shooter.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor

private const val DEFAULT_FRAME_DURATION = 1 / 20f

/** AnimationType
* @param speedRate Time given for 1 frame in the animation timeline
* */
enum class AnimationType(
    val atlasKey: String, val playMode: Animation.PlayMode = Animation.PlayMode.LOOP, val speedRate: Float = 1f
) {
    NONE(""),
    DARK_MATTER("dark_matter")
}

class Animation2D(
    val type: AnimationType,
    keyFrames: com.badlogic.gdx.utils.Array<out TextureRegion>,
    playMode: PlayMode = PlayMode.LOOP,
    speedRate: Float = 1f
) : Animation<TextureRegion>(DEFAULT_FRAME_DURATION / speedRate, keyFrames, playMode)

class AnimationComponent : Component, Poolable {
    var type = AnimationType.NONE
    var stateTime = 0f
    lateinit var animation: Animation2D

    override fun reset() {
        type = AnimationType.NONE
        stateTime = 0f
    }

    companion object {
        val mapper = mapperFor<AnimationComponent>()
    }
}