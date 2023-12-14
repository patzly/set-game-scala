package de.htwg.se.set.model

case class Game(columns: Int, deck: Deck, tableCards: List[Card], playersCards: List[Card], players: List[Player], selectedPlayer: Option[Player], message: String = ""):

  override def toString: String = "\n" + Grid(columns, tableCards, deck.easy)