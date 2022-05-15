package space.shooter.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.log.logger
import space.shooter.SpaceShooterGame

private val LOG = logger<SecondScreen>()

class SecondScreen(game: SpaceShooterGame) : GameScreen(game) {
    override fun show() {
        LOG.debug { "Second Screen shown" }
    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            game.setScreen<FirstScreen>()
        }
    }
}