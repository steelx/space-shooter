package space.shooter.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import ktx.ashley.*
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.log.logger
import space.shooter.V_WIDTH
import space.shooter.ecs.components.*
import kotlin.math.min

private val LOG = logger<PowerUpSystem>()
private const val MAX_SPAWN_INTERVAL = 2.5f
private const val MIN_SPAWN_INTERVAL = 1.5f
private const val POWER_UP_SPEED = -5.75f
private const val BOOST_1_SPEED_GAIN = 2.5f
private const val BOOST_2_SPEED_GAIN = 3.75f
private const val LIFE_GAIN = 25f
private const val SHIELD_GAIN = 25f

private class SpawnPattern(
    type1: PowerUpType = PowerUpType.NONE,
    type2: PowerUpType = PowerUpType.NONE,
    type3: PowerUpType = PowerUpType.NONE,
    type4: PowerUpType = PowerUpType.NONE,
    type5: PowerUpType = PowerUpType.NONE,
    val types: GdxArray<PowerUpType> = gdxArrayOf(type1, type2, type3, type4, type5)
)

class PowerUpSystem :
    IteratingSystem(allOf(PowerUpComponent::class, TransformComponent::class).exclude(RemoveComponent::class).get()) {
    private val playerBoundingRect = Rectangle()
    private val powerUpBoundingRect = Rectangle()
    private val playerEntities by lazy {
        engine.getEntitiesFor(allOf(PlayerComponent::class).exclude(RemoveComponent::class).get())
    }

    private var spawnTime = 0f
    private val spawnPatterns = gdxArrayOf<SpawnPattern>(
        SpawnPattern(type1 = PowerUpType.SPEED_1, type2 = PowerUpType.SPEED_2, type5 = PowerUpType.LIFE),
        SpawnPattern(type2 = PowerUpType.LIFE, type3 = PowerUpType.SHIELD, type4 = PowerUpType.SPEED_2),
        SpawnPattern(type2 = PowerUpType.SHIELD, type4 = PowerUpType.SPEED_1, type5 = PowerUpType.SPEED_2)
    )
    private val currentSpawnPattern = gdxArrayOf<PowerUpType>()

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        spawnTime -= deltaTime
        if (spawnTime <= 0f) {
            spawnTime = MathUtils.random(MIN_SPAWN_INTERVAL, MAX_SPAWN_INTERVAL)

            if (currentSpawnPattern.isEmpty) {
                currentSpawnPattern.addAll(spawnPatterns[MathUtils.random(0, spawnPatterns.size-1)].types)
                LOG.debug { "Next spawn pattern: $currentSpawnPattern" }
            }

            val powerUpType = currentSpawnPattern.removeIndex(0)
            if (powerUpType == PowerUpType.NONE) {
               return// nothing to spawn
            }

            spawnPowerUp(powerUpType, 1f * MathUtils.random(0f, V_WIDTH-1), 16f)
        }
    }

    private fun spawnPowerUp(powerUpType: PowerUpType, x: Float, y: Float) {
        engine.entity {
            with<TransformComponent> { setInitPosition(x, y, 0f) }
            with<PowerUpComponent> { type = powerUpType }
            with<AnimationComponent> { type = powerUpType.animationType }
            with<GraphicsComponent>()
            with<MoveComponent> { speed.y = POWER_UP_SPEED }
        }
    }

    // iterate over allOf definations, entity here are all powerUps
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) {
            "Entity |entity| must have TransformComponent. entity=$entity"
        }

        // if position of this entity is at bottom we simply remove it
        if (transform.position.y <= 1f) {
            entity.addComponent<RemoveComponent>(engine)
            return
        }

        powerUpBoundingRect.set(transform.position.x, transform.position.y, transform.size.x, transform.size.y)

        // check if does not collide with player
        playerEntities.forEach {player ->
            player[TransformComponent.mapper]?.let {
                playerBoundingRect.set(it.position.x, it.position.y, it.size.x, it.size.y)
                if (playerBoundingRect.overlaps(powerUpBoundingRect)) {
                    collectPowerUp(player, entity)
                }
            }
        }
    }

    private fun collectPowerUp(player: Entity, powerUp: Entity) {
        val powerUpCmp = powerUp[PowerUpComponent.mapper]
        require( powerUpCmp != null) {
            "Entity |entity| must have PowerUpComponent. entity=$powerUp"
        }

        LOG.debug { "Picking up power up of type: ${powerUpCmp.type}" }

        when (powerUpCmp.type) {
            PowerUpType.SPEED_1 -> {
                player[MoveComponent.mapper]?.let { it.speed.y += BOOST_1_SPEED_GAIN }
            }
            PowerUpType.SPEED_2 -> {
                player[MoveComponent.mapper]?.let { it.speed.y += BOOST_2_SPEED_GAIN }
            }
            PowerUpType.LIFE -> {
                player[PlayerComponent.mapper]?.let { it.life = min(it.maxLife, it.life + LIFE_GAIN) }
            }
            PowerUpType.SHIELD -> {
                player[PlayerComponent.mapper]?.let { it.shield = min(it.maxShield, it.shield + SHIELD_GAIN) }
            }
            else -> {
                LOG.error { "Unsupported power up type: ${powerUpCmp.type}" }
            }
        }

        // once power has been applied, remove it
        powerUp.addComponent<RemoveComponent>(engine)
    }

}