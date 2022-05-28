package space.shooter.screens

import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.logger
import space.shooter.SpaceShooterGame
import space.shooter.ecs.components.*
import kotlin.math.min

private val LOG = logger<FirstScreen>()
private const val MAX_DELTA_TIME = 1 / 25f // 25 FPS

class FirstScreen(game: SpaceShooterGame) : GameScreen(game) {

    override fun show() {
        LOG.debug { "First Screen shown" }

        // Player
        engine.entity {
            with<TransformComponent> {
                setInitPosition(10f, 10f, 0f)
            }
            with<GraphicsComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
            with<MoveComponent>()
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
    }


    override fun render(deltaTime: Float) {
        engine.update(min(MAX_DELTA_TIME, deltaTime))
    }

    override fun dispose() {
    }
}