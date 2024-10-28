package de.htwg.se.set

import com.google.inject.Guice
import de.htwg.se.set.controller.IController
import de.htwg.se.set.module.SetModule
import de.htwg.se.set.view.{Gui, Tui}

import java.awt.GraphicsEnvironment

object Set:
  
  def main(args: Array[String]): Unit =
    val injector = Guice.createInjector(new SetModule)
    val controller = injector.getInstance(classOf[IController])
    if !GraphicsEnvironment.isHeadless then Gui(controller)
    Tui(controller)