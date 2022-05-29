package space.shooter.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import space.shooter.ecs.components.PlayerComponent
import space.shooter.ecs.components.RemoveComponent
import space.shooter.ecs.components.TransformComponent
import kotlin.math.max

const val DAMAGE_AREA_HEIGHT = 1f
private const val DAMAGE_PER_SECONDS = 25f
private const val DEATH_EXPLOSION_DURATION = 0.9f

class DamageSystem :
    IteratingSystem(allOf(PlayerComponent::class, TransformComponent::class).exclude(RemoveComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) {
            "Entity |entity| must have TransformComponent. Entity=$entity"
        }
        val player = entity[PlayerComponent.mapper]
        require(player != null) {
            "Entity |entity| must have PlayerComponent. Entity=$entity"
        }

        if (transform.position.y <= DAMAGE_AREA_HEIGHT) {
            var damage = DAMAGE_PER_SECONDS * deltaTime

            if (player.shield > 0f) {
                val blockedAmount = player.shield
                player.shield = max(0f, player.shield - damage)
                damage -= blockedAmount
                if (damage <= 0) {
                    return
                }
            }

            player.life -= damage
            if (player.life <= 0f) {
                entity.addComponent<RemoveComponent>(engine) {
                    delay = DEATH_EXPLOSION_DURATION
                }
            }
        }
    }
}