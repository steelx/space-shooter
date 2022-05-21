package space.shooter.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import space.shooter.ecs.components.RemoveComponent

class RemoveSystem : IteratingSystem(allOf(RemoveComponent::class).get()){

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val removeComponent = entity[RemoveComponent.mapper]
        require(removeComponent != null) {
            "Entity |entity| must have RemoveComponent. Entity=$entity"
        }

        removeComponent.delay -= deltaTime
        if (removeComponent.delay <= 0) {
            engine.removeEntity(entity)
        }
    }
}