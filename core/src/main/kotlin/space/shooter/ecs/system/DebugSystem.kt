package space.shooter.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.getSystem
import space.shooter.ecs.components.PlayerComponent
import space.shooter.ecs.components.TransformComponent
import kotlin.math.max
import kotlin.math.min

private const val UPDATE_RATE = 0.25f// 4 times per second

class DebugSystem : IntervalIteratingSystem(allOf(PlayerComponent::class).get(), UPDATE_RATE){
    init {
        setProcessing(true)// enable/disable debug
    }

    override fun processEntity(entity: Entity) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) {
            "Entity |entity| must have TransformComponent. Entity=$entity"
        }
        val player = entity[PlayerComponent.mapper]
        require(player != null) {
            "Entity |entity| must have PlayerComponent. Entity=$entity"
        }

        when {
            Gdx.input.isKeyPressed(Input.Keys.NUM_1) -> {
                // KILL Player
                player.life = 0f
                player.shield = 0f
                transform.position.y = 1f
            }

            Gdx.input.isKeyPressed(Input.Keys.NUM_2) -> {
                // Add Shield
                player.shield = min(player.maxShield, player.shield + 25f)
            }

            Gdx.input.isKeyPressed(Input.Keys.NUM_3) -> {
                // Remove Shield
                player.shield = max(0f, player.shield - 25f)
            }
            Gdx.input.isKeyPressed(Input.Keys.NUM_4) -> {
                // Disable Movement
                engine.getSystem<MoveSystem>().setProcessing(false)
            }
            Gdx.input.isKeyPressed(Input.Keys.NUM_5) -> {
                // Enable Movement
                engine.getSystem<MoveSystem>().setProcessing(true)
            }
        }

        Gdx.graphics.setTitle("Debug: H: ${player.life} S: ${player.shield} Pos: ${transform.position}")

    }
}