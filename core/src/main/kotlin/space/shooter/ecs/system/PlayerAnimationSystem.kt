package space.shooter.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.logger
import space.shooter.ecs.components.*

private val LOG = logger<PlayerAnimationSystem>()

class PlayerAnimationSystem(
    private val defaultRegion: TextureRegion,
    private val leftRegion: TextureRegion,
    private val rightRegion: TextureRegion
) : IteratingSystem(
    allOf(PlayerComponent::class, GraphicsComponent::class, FacingComponent::class, TransformComponent::class).get()
), EntityListener {

    private var lastDirection = FacingDirection.DEFAULT

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) {
        entity[GraphicsComponent.mapper]?.setSpriteRegion(defaultRegion)
    }

    override fun entityRemoved(entity: Entity?) = Unit

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val facing = entity[FacingComponent.mapper]
        require(facing != null) { "Entity |entity| must have a FacingComponent. entity=$entity" }
        val graphic = entity[GraphicsComponent.mapper]
        require(graphic != null) { "Entity |entity| must have a GraphicComponent. entity=$entity" }

        if (facing.direction == lastDirection && graphic.sprite.texture != null) {
            // texture already set and direction does not change
            return
        }

        lastDirection = facing.direction
        val region = when(facing.direction) {
            FacingDirection.LEFT -> leftRegion
            FacingDirection.RIGHT -> rightRegion
            else -> defaultRegion
        }
        graphic.setSpriteRegion(region)
    }
}