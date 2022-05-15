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
import space.shooter.ecs.components.GraphicsComponent
import space.shooter.ecs.components.TransformComponent

private val LOG = logger<FirstScreen>()

class FirstScreen(game: SpaceShooterGame) : GameScreen(game) {

    private val playerTex = Texture(Gdx.files.internal("assets/player_ship/Player_ship_16.png"))
    private val player = engine.entity {
        with<TransformComponent> {
            position.set(10f, 1f, 0f)
        }
        with<GraphicsComponent> {
            sprite.run {
                setRegion(playerTex)
                setSize(playerTex.width* UNIT_SCALE, playerTex.height* UNIT_SCALE)
                setOrigin(2f, 2f)
            }
        }
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