package de.htwg.se.set

import de.htwg.se.set.controller.controller.Controller
import de.htwg.se.set.model.{ICard, IPlayer}
import de.htwg.se.set.model.game.{Deck, Game}
import de.htwg.se.set.model.settings.Settings
import de.htwg.se.set.view.{Gui, Tui}

@main
def main(): Unit =
  val settings = Settings(1, false)
  val game = Game(4, Deck(false), List[ICard](), List[ICard](), List[IPlayer](), None)
  val controller = Controller(settings, game)
  Gui(controller)
  Tui(controller)