package de.htwg.se.set

import com.google.inject.Guice
import de.htwg.se.set.controller.IController
import de.htwg.se.set.module.SetModule
import de.htwg.se.set.view.{Gui, Tui}

@main
def main(): Unit =
  val controller = Guice.createInjector(new SetModule).getInstance(classOf[IController])
  Gui(controller)
  Tui(controller)