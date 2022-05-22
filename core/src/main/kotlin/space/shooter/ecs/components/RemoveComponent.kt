package space.shooter.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor

/**
 * RemoveComponent is needed for Pooled engines
 * where this avoids strange behaviour if entity removed directly.
 * used with: @link RemoveSystem
 * Whenever any entity needs to be removed one need to
 * add RemoveComponent to that entity e.g. ->
 * entity.addComponent<RemoveComponent>(engine) { delay: 1f}
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
