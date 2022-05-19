package space.shooter.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ktx.ashley.get
import space.shooter.ecs.components.FacingComponent
import space.shooter.ecs.components.FacingDirection
import space.shooter.ecs.components.PlayerComponent
import space.shooter.ecs.components.TransformComponent

private const val TOUCH_TOLERANCE_DISTANCE = 0.8f

class PlayerInputSystem(private val gameViewport: Viewport) : IteratingSystem(
    allOf(PlayerComponent::class, FacingComponent::class, TransformComponent::class).get()
) {
    private val tempVec2 = Vector2(0f, 0f)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val facing = entity[FacingComponent.mapper]
        require(facing != null) {
            "Entity |entity| must have FacingComponent. Entity=$entity"
        }

        val transform = entity[TransformComponent.mapper]
        require(transform != null) {
            "Entity |entity| must have TransformComponent. Entity=$entity"
        }

        tempVec2.x = Gdx.input.x.toFloat()
        gameViewport.unproject(tempVec2)
        val diffX = tempVec2.x - transform.position.x - transform.size.x*0.5f
        facing.direction = when {
            diffX > TOUCH_TOLERANCE_DISTANCE -> FacingDirection.RIGHT
            diffX < -TOUCH_TOLERANCE_DISTANCE -> FacingDirection.LEFT
            else -> FacingDirection.DEFAULT
        }
    }
}