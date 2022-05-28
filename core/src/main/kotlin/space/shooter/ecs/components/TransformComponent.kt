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
    val prevPosition = vec3()
    val interpolatedPosition = vec3()//diff btw position and prevPosition
    val size = vec2(1f, 1f)// Scale factor
    var rotationDegree = 0f

    override fun reset() {
        rotationDegree = 0f
        setInitPosition(0f, 0f, 0f)
        size.set(1f, 1f)
    }

    /// compareTo - sorts 2 entities so the front one renders correctly
    override fun compareTo(other: TransformComponent): Int {
        val zDiff = other.position.z.compareTo(position.z)
        return if (zDiff == 0) other.position.y.compareTo(position.y) else zDiff
    }

    companion object {
        val mapper = mapperFor<TransformComponent>()
    }

    fun setInitPosition(x: Float, y: Float, z: Float) {
        position.set(x, y, z)
        prevPosition.set(x, y, z)
        interpolatedPosition.set(x, y, z)
    }
}