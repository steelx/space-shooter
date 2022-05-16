package space.shooter.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor

class FacingComponent : Component, Poolable{
    var direction = FacingDirection.DEFAULT

    override fun reset() {
        direction = FacingDirection.DEFAULT
    }

    companion object {
        val mapper = mapperFor<FacingComponent>()
    }
}

enum class FacingDirection {
    LEFT, DEFAULT, RIGHT
}