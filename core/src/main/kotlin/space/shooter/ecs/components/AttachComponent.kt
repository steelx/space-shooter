package space.shooter.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor
import ktx.math.vec2

class AttachComponent : Component, Poolable {
    /**
     * @param entity will be engine.entity
     * e.g. Player entity
     * */
    lateinit var entity: Entity
    val offset = vec2(0f, 0f)

    override fun reset() {
        offset.set(0f, 0f)
    }

    companion object {
        val mapper = mapperFor<AttachComponent>()
    }

}