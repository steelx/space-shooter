package space.shooter.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor
import ktx.math.vec2
import ktx.math.vec3

class TransformComponent : Component, Poolable, Comparable<TransformComponent> {
    // Dont use Vector3.Zero for initialization, its a shared mutable instance
    val position = vec3()
    val size = vec2(1f, 1f)// Scale factor
    var rotationDegree = 0f

    override fun reset() {
        rotationDegree = 0f
        position.set(Vector3.Zero)
        size.set(1f, 1f)
    }

    override fun compareTo(other: TransformComponent): Int {
        val zDiff = other.position.z.compareTo(position.z)
        return if (zDiff == 0) other.position.y.compareTo(position.y) else zDiff
    }

    companion object {
        val mapper = mapperFor<TransformComponent>()
    }
}