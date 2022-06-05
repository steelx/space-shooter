package space.shooter.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.GdxRuntimeException
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.logger
import space.shooter.ecs.components.Animation2D
import space.shooter.ecs.components.AnimationComponent
import space.shooter.ecs.components.AnimationType
import space.shooter.ecs.components.GraphicsComponent
import java.util.EnumMap

private val LOG = logger<AnimationSystem>()

class AnimationSystem(private val atlas: TextureAtlas) :
    IteratingSystem(allOf(AnimationComponent::class, GraphicsComponent::class).get()), EntityListener {

    val animationsCache = EnumMap<AnimationType, Animation2D>(AnimationType::class.java)

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityRemoved(entity: Entity) = Unit

    override fun entityAdded(entity: Entity) {
        // initialize Entity with certain Animation & Graphics
        entity[AnimationComponent.mapper]?.let {
            it.animation = getAnimation(it.type)
            val frame = it.animation.getKeyFrame(it.stateTime)
            entity[GraphicsComponent.mapper]?.setSpriteRegion(frame)
        }
    }

    private fun getAnimation(type: AnimationType): Animation2D {
        var animation = animationsCache[type]
        if (animation == null) {
            // load animation
            var regions = atlas.findRegions(type.atlasKey)
            if (regions.isEmpty) {
                LOG.debug {
                    "No regions found for Animation ${type.atlasKey}"
                }
                regions = atlas.findRegions("error")
                if (regions.isEmpty) throw GdxRuntimeException("Looks like you forgot to add Error regions")
            } else {
                LOG.debug { "Loading new Animation of type: ${type} with (${regions.size}) regions." }
            }
            animation = Animation2D(type, regions, type.playMode, type.speedRate)
            animationsCache[type] = animation
        }
        return animation
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        // update Animation frame
        val animationComponent = entity[AnimationComponent.mapper]
        require(animationComponent != null) { "Entity |entity| must have a AnimationComponent. entity=$entity" }
        val graphic = entity[GraphicsComponent.mapper]
        require(graphic != null) { "Entity |entity| must have a GraphicComponent. entity=$entity" }

        if (animationComponent.type == AnimationType.NONE) {
            LOG.error {
                "No AnimationType specified for type $animationComponent for |entity| $entity"
            }
            return
        }

        if (animationComponent.type == animationComponent.animation.type) {
            // still in current animation, update it
            animationComponent.stateTime += deltaTime
        } else {
            // change to new animation
            animationComponent.stateTime = 0f
            animationComponent.animation = getAnimation(animationComponent.type)
        }

        val frame = animationComponent.animation.getKeyFrame(animationComponent.stateTime)
        graphic.setSpriteRegion(frame)
    }

}