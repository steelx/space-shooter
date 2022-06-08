package space.shooter.screens

import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.logger
import space.shooter.SpaceShooterGame
import space.shooter.UNIT_SCALE
import space.shooter.V_WIDTH
import space.shooter.ecs.components.*
import space.shooter.ecs.system.DAMAGE_AREA_HEIGHT
import kotlin.math.min

private val LOG = logger<FirstScreen>()
private const val MAX_DELTA_TIME = 1 / 25f // 25 FPS

class FirstScreen(game: SpaceShooterGame) : GameScreen(game) {

    override fun show() {
        LOG.debug { "First Screen shown" }

        // Player
        val playerShip = engine.entity {
            with<TransformComponent> {
                setInitPosition(10f, 10f, -1f)// -1 so that ship zIndex comes above ship fire
            }
            with<GraphicsComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
            with<MoveComponent>()
        }
        // Player Ship Fire
        engine.entity {
            with<AttachComponent> {
                entity = playerShip
                offset.set(0f, -8 * UNIT_SCALE)
            }
            with<TransformComponent>()
            with<GraphicsComponent>()
            with<AnimationComponent> {
                type = AnimationType.SHIP_FIRE
            }
        }

        // Enemy
        engine.entity {
            with<TransformComponent> {
                setInitPosition(4f, 12f, 0f)
            }
            with<GraphicsComponent> {
                setSpriteRegion(game.graphicsAtlas.findRegion("fighter"))
            }
        }

        // Enemy
        engine.entity {
            with<TransformComponent> {
                setInitPosition(11f, 7f, 0f)
            }
            with<GraphicsComponent> {
                setSpriteRegion(game.graphicsAtlas.findRegion("parafighter"))
            }
        }

        // Bottom Dark matter
        engine.entity {
            with<TransformComponent> {
                size.set(V_WIDTH.toFloat(), DAMAGE_AREA_HEIGHT)
                setInitPosition(0f, 0f, 0f)
            }
            with<GraphicsComponent>()
            with<AnimationComponent> {
                type = AnimationType.DARK_MATTER
            }
        }
    }


    override fun render(deltaTime: Float) {
        engine.update(min(MAX_DELTA_TIME, deltaTime))
    }

    override fun dispose() {
    }
}