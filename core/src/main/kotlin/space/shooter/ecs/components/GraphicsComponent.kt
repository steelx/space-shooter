package space.shooter.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor

class GraphicsComponent : Component, Poolable {
    val sprite = Sprite()

    override fun reset() {
        sprite.texture = null
    }

    companion object {
        val mapper = mapperFor<GraphicsComponent>()
    }
}