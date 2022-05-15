@file:JvmName("Lwjgl3Launcher")

package space.shooter.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import space.shooter.SpaceShooterGame

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(SpaceShooterGame(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("SpaceShooter")
        setWindowedMode(20*32, 15*32)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
