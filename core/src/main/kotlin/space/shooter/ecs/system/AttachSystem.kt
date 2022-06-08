package space.shooter.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.get
import space.shooter.ecs.components.AttachComponent
import space.shooter.ecs.components.GraphicsComponent
import space.shooter.ecs.components.RemoveComponent
import space.shooter.ecs.components.TransformComponent

class AttachSystem :
    IteratingSystem(allOf(AttachComponent::class, TransformComponent::class, GraphicsComponent::class).get()),
    EntityListener {

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        // when not passing family, we mean any kind of Enitty that gets passed here
        engine.addEntityListener(this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) = Unit

    override fun entityRemoved(removedEntity: Entity) {
        // loop over all entities in all systems
        entities.forEach { entity ->
            entity[AttachComponent.mapper]?.let {
                if (it.entity == removedEntity) {
                    // e.g. removedEntity might be Player
                    // so we remove the entity, which might be the fire entity
                    entity.addComponent<RemoveComponent>(engine)
                }
            }
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val attach = entity[AttachComponent.mapper]
        require(attach != null) {
            "Entity |entity| must have AttachComponent. Entity=$entity"
        }
        val transform = entity[TransformComponent.mapper]
        require(transform != null) {
            "Entity |entity| must have TransformComponent. Entity=$entity"
        }
        val graphics = entity[GraphicsComponent.mapper]
        require(graphics != null) {
            "Entity |entity| must have GraphicsComponent. Entity=$entity"
        }

        // update position
        attach.entity[TransformComponent.mapper]?.let { parentTransform ->
            transform.interpolatedPosition.set(
                parentTransform.interpolatedPosition.x + attach.offset.x,
                parentTransform.interpolatedPosition.y + attach.offset.y,
                transform.position.z
            )
        }

        // update graphics alpha as per its parent
        attach.entity[GraphicsComponent.mapper]?.let { attachedGraphic ->
            graphics.sprite.setAlpha(attachedGraphic.sprite.color.a)
        }
    }
}