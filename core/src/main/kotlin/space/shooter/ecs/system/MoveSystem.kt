package space.shooter.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import space.shooter.V_HEIGHT
import space.shooter.V_WIDTH
import space.shooter.ecs.components.*
import kotlin.math.max
import kotlin.math.min

private const val FPS = 1 / 25f
private const val HORIZONTAL_ACCELERATION = 16.5f
private const val VERTICAL_ACCELERATION = 2.25f
private const val MAX_VERTICAL_NEG_SPEED = 0.75f
private const val MAX_VERTICAL_POS_SPEED = 5f
private const val MAX_HORIZONTAL_SPEED = 5.5f

class MoveSystem :
    IteratingSystem(allOf(MoveComponent::class, TransformComponent::class).exclude(RemoveComponent::class).get()) {
    private var accumulator = 0f

    /*
    * overriding update with a fixed timestep
    * for better updating physics based entities
    * */
    override fun update(deltaTime: Float) {
        accumulator += deltaTime
        while (accumulator >= FPS) {
            accumulator -= FPS

            entities.forEach {
                it[TransformComponent.mapper]?.let {transform ->
                    transform.prevPosition.set(transform.position)
                }
            }

            super.update(FPS)
        }

        val alpha = accumulator / FPS // value btw 0..0.99.1
        entities.forEach { entity ->
            entity[TransformComponent.mapper]?.let { transform ->
                transform.interpolatedPosition.set(
                    MathUtils.lerp(transform.prevPosition.x, transform.position.x, alpha),
                    MathUtils.lerp(transform.prevPosition.y, transform.position.y, alpha),
                    transform.position.z
                )
            }
        }
    }

    override fun processEntity(entity: Entity, fpsValue: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) {
            "Entity |entity| must have TransformComponent. Entity=$entity"
        }
        val move = entity[MoveComponent.mapper]
        require(move != null) {
            "Entity |entity| must have MoveComponent. Entity=$entity"
        }

        val player = entity[PlayerComponent.mapper]
        if (player != null) {
            // do player movement
            entity[FacingComponent.mapper]?.let { facing ->
                movePlayer(transform, move, player, facing, fpsValue)
            }
        } else {
            // do power-up moves
            moveEntity(transform, move, fpsValue)
        }
    }

    private fun movePlayer(
        transform: TransformComponent,
        move: MoveComponent,
        player: PlayerComponent,
        facing: FacingComponent,
        deltaTime: Float
    ) {
        // TODO: player powerups
        // update horizontal speed
        move.speed.x = when (facing.direction) {
            FacingDirection.LEFT -> min(0f, move.speed.x - HORIZONTAL_ACCELERATION * deltaTime)
            FacingDirection.RIGHT -> max(0f, move.speed.x + HORIZONTAL_ACCELERATION * deltaTime)
            else -> 0f
        }
        move.speed.x = MathUtils.clamp(move.speed.x, -MAX_HORIZONTAL_SPEED, MAX_HORIZONTAL_SPEED)

        // update vertical speed
        move.speed.y =
            MathUtils.clamp(
                move.speed.y - VERTICAL_ACCELERATION * deltaTime,
                -MAX_VERTICAL_NEG_SPEED,
                MAX_VERTICAL_POS_SPEED
            )

        moveEntity(transform, move, deltaTime)
    }

    private fun moveEntity(transform: TransformComponent, move: MoveComponent, deltaTime: Float) {
        transform.position.x = MathUtils.clamp(
            transform.position.x + move.speed.x * deltaTime,
            0f,
            V_WIDTH - transform.size.x
        )
        transform.position.y = MathUtils.clamp(
            transform.position.y + move.speed.y * deltaTime,
            1f,
            V_HEIGHT + 1f - transform.size.y
        )
    }
}