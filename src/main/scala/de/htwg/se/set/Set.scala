package de.htwg.se.set

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.view.Tui
import util.PrintUtil
import model.{Card, Deck, Game, Player, Settings, Triplet}

@main
def main(): Unit =
  println(PrintUtil.bold("Welcome to the SET Game!"))

  val settings = Settings(1, false)

  val deck = Deck(false)
  val cards = deck.tableCardsSinglePlayer(12)
  val players = List[Player](Player(1, true, false, List[Triplet]()))
  val game = Game(4, deck, cards, List[Card](), players)

  val controller = Controller(settings, game)
  Tui(controller).run()