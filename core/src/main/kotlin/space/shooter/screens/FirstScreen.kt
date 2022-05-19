package space.shooter.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.logger
import space.shooter.SpaceShooterGame
import space.shooter.ecs.components.FacingComponent
import space.shooter.ecs.components.GraphicsComponent
import space.shooter.ecs.components.PlayerComponent
import space.shooter.ecs.components.TransformComponent

private val LOG = logger<FirstScreen>()

class FirstScreen(game: SpaceShooterGame) : GameScreen(game) {

    override fun show() {
        LOG.debug { "First Screen shown" }

        // Player
        engine.entity {
            with<TransformComponent> {
                position.set(10f, 1f, 0f)
            }
            with<GraphicsComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
        }

        // Enemy
        engine.entity {
            with<TransformComponent> {
                position.set(4f, 12f, 0f)
            }
            with<GraphicsComponent> {
                setSpriteRegion(game.graphicsAtlas.findRegion("fighter"))
            }
        }

        // Enemy
        engine.entity {
            with<TransformComponent> {
                position.set(11f, 7f, 0f)
            }
            with<GraphicsComponent> {
                setSpriteRegion(game.graphicsAtlas.findRegion("parafighter"))
            }
        }
    }


    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            game.setScreen<SecondScreen>()
        }
        engine.update(delta)
    }

    override fun dispose() {
    }
}