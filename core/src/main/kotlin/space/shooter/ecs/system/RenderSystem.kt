package space.shooter.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.viewport.Viewport
import space.shooter.ecs.components.GraphicsComponent
import space.shooter.ecs.components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use
import ktx.log.logger

private val LOG = logger<RenderSystem>()

class RenderSystem(private val batch: Batch, private val gameViewport: Viewport) : SortedIteratingSystem(
    allOf(TransformComponent::class, GraphicsComponent::class).get(),
    compareBy { entity -> entity[TransformComponent.mapper] }
) {

    override fun update(deltaTime: Float) {
        forceSort()
        gameViewport.apply()
        batch.use(gameViewport.camera.combined) {
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) {
            "Entity |entity| must have a TransformComponent. Entity=$entity"
        }

        /*
        * we can also use kotlin's elvis operator with 'let' strategy
        * to check Null pointer in option to `require` method
        * entity[GraphicsComponent.mapper]?.let { graphics ->
        *   entity[TransformComponent.mapper]?.let { transform -> ... }
        * }
        * */
        val graphics = entity[GraphicsComponent.mapper]
        require(graphics != null) {
            "Entity |entity| must have a GraphicsComponent. Entity=$entity"
        }

        if (graphics.sprite.texture == null) {
            LOG.error { "Looks like you forgot to pass Texture to your Sprite in Entity=$entity" }
            throw GdxRuntimeException("Looks like you forgot to pass Texture to your Sprite in Entity=$entity")
        }

        graphics.sprite.run {
            rotation = transform.rotationDegree
            setBounds(transform.interpolatedPosition.x, transform.interpolatedPosition.y, transform.size.x, transform.size.y)
            draw(batch)
        }
    }
}


