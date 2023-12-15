package de.htwg.se.set

import de.htwg.se.set.controller.controllerComponent.Controller
import de.htwg.se.set.modelComponent.gameComponent.{Deck, Game}
import de.htwg.se.set.modelComponent.{Settings, gameComponent}
import de.htwg.se.set.view.{Gui, Tui}

@main
def main(): Unit =
  val settings = Settings(1, false)
  val game = gameComponent.Game(4, Deck(false), List(), List(), List(), None)
  val controller = Controller(settings, game)
  Gui(controller)
  Tui(controller)