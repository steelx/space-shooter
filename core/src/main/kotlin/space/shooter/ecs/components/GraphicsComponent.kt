package space.shooter.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor
import space.shooter.UNIT_SCALE

class GraphicsComponent : Component, Poolable {
    val sprite = Sprite()

    override fun reset() {
        sprite.texture = null
    }

    fun setSpriteRegion(region: TextureRegion) {
        sprite.run {
            setRegion(region)
            setSize(region.regionWidth * UNIT_SCALE, region.regionHeight * UNIT_SCALE)
            setOriginCenter()
        }
    }

    companion object {
        val mapper = mapperFor<GraphicsComponent>()
    }
}