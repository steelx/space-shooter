package space.shooter

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.logger
import space.shooter.ecs.system.*
import space.shooter.screens.FirstScreen
import space.shooter.screens.SecondScreen

private val LOG = logger<SpaceShooterGame>()
const val V_WIDTH = 20f
const val V_HEIGHT = 15f
const val UNIT_SCALE = 1 / 16f

class SpaceShooterGame : KtxGame<KtxScreen>() {

    val graphicsAtlas by lazy { TextureAtlas(Gdx.files.internal("assets/atlas/space-shooter.atlas")) }

    // `by lazy` initialize Batch only when it's called
    // smart way to avoid calling it inside create
    val batch: Batch by lazy { SpriteBatch() }
    val viewport = FitViewport(V_WIDTH, V_HEIGHT)
    val engine: Engine by lazy {
        // Tip: `apply` is passes all needed args to function without calling
        // `run` will actually run it right away
       PooledEngine().apply {
           addSystem(PlayerInputSystem(viewport))
           addSystem(MoveSystem())
           addSystem(DamageSystem())
           addSystem(AnimationSystem(graphicsAtlas))
           addSystem(
               PlayerAnimationSystem(
                   graphicsAtlas.findRegion("ship2"),
                   graphicsAtlas.findRegion("ship1"),
                   graphicsAtlas.findRegion("ship3")
               )
           )
           addSystem(RenderSystem(batch, viewport))
           addSystem(RemoveSystem())
           addSystem(DebugSystem())
       }
    }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG

        LOG.debug { "SpaceShooterKtx create" }

        addScreen(FirstScreen(this))
        addScreen(SecondScreen(this))
        setScreen<FirstScreen>()
    }

    override fun dispose() {
        batch.dispose()
        graphicsAtlas.dispose()
    }
}
