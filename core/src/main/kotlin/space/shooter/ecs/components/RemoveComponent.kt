package space.shooter.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor

/**
 * RemoveComponent is needed for Pooled engines
 * where this avoids strange behaviour if entity removed directly.
 * used with: @link RemoveSystem
 * */
class RemoveComponent : Component, Poolable {
    var delay = 0f

    override fun reset() {
        delay = 0f
    }

    companion object {
        val mapper = mapperFor<RemoveComponent>()
    }

}
