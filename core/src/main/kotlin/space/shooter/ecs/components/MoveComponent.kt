package space.shooter.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor
import ktx.math.vec2

/*
* MoveComponent used with @link MoveSystem
* */
class MoveComponent : Component, Poolable {
    val speed = vec2()

    override fun reset() {
        speed.set(0f, 0f)
    }

    companion object {
        val mapper = mapperFor<MoveComponent>()
    }
}