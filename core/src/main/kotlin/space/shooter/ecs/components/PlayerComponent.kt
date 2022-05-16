package space.shooter.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor

private val MAX_LIFE = 100f
private val MAX_SHIELD = 100f

class PlayerComponent : Component, Poolable {

    var life = MAX_LIFE
    var maxLife = MAX_LIFE
    var shield = 0f
    var maxShield = MAX_SHIELD
    var distanceTraveled = 0f

    override fun reset() {
        life = MAX_LIFE
        maxLife = MAX_LIFE
        shield = 0f
        maxShield = MAX_SHIELD
        distanceTraveled = 0f
    }

    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }
}