package de.htwg.se.set

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.{Deck, Game, Settings}
import de.htwg.se.set.view.{Gui, Tui}

@main
def main(): Unit =
  val settings = Settings(1, false)
  val game = Game(4, Deck(false), List(), List(), List(), None)
  val controller = Controller(settings, game)
  Gui(controller)
  Tui(controller)