package de.htwg.se.set.model

case class Game(rows: Int, columns: Int, deck: Deck, tableCards: List[Card], playersCards: List[Card], players: List[Player]):
  
  def cellCount: Int = rows * columns

  override def toString: String = "\n" + Grid(rows, columns, tableCards, deck.easy)