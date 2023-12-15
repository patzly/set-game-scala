package de.htwg.se.set.modelComponent.gameComponent

import de.htwg.se.set.modelComponent.Card

case class Game(columns: Int, deck: Deck, tableCards: List[Card], playersCards: List[Card], players: List[Player], selectedPlayer: Option[Player], message: String = ""):

  override def toString: String = "\n" + Grid(columns, tableCards, deck.easy)