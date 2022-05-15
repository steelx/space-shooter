package space.shooter.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.Batch
import ktx.app.KtxScreen
import ktx.log.logger
import space.shooter.SpaceShooterGame

private val LOG = logger<GameScreen>()
abstract class GameScreen(
    val game: SpaceShooterGame,
    val batch: Batch = game.batch,
    val engine: Engine = game.engine
) : KtxScreen {

    override fun dispose() {
        batch.dispose()
    }

    override fun resize(width: Int, height: Int) {
        game.viewport.update(width, height, true)
    }
}