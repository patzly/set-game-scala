package de.htwg.se.set.model

case class Game(rows: Int, columns: Int, deck: Deck, tableCards: List[Card], playersCards: List[Card], players: List[Player]):

  override def toString: String = "\n" + Grid(rows, columns, tableCards, deck.easy)