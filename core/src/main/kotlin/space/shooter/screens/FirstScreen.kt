package space.shooter.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.logger
import space.shooter.SpaceShooterGame
import space.shooter.UNIT_SCALE
import space.shooter.V_HEIGHT
import space.shooter.V_WIDTH
import space.shooter.ecs.components.FacingComponent
import space.shooter.ecs.components.GraphicsComponent
import space.shooter.ecs.components.PlayerComponent
import space.shooter.ecs.components.TransformComponent

private val LOG = logger<FirstScreen>()

class FirstScreen(game: SpaceShooterGame) : GameScreen(game) {

    private val player = engine.entity {
        with<TransformComponent> {
            position.set(10f, 1f, 0f)
        }
        with<GraphicsComponent>()
        with<PlayerComponent>()
        with<FacingComponent>()
    }

    override fun show() {
        LOG.debug { "First Screen shown" }
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