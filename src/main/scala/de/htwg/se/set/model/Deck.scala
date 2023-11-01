package de.htwg.se.set.model

import scala.util.Random

class Deck(easy: Boolean):

  val allCards: List[Card] =
    if easy then
      val all = for
        number <- 1 to 3
        color <- Color.values
        symbol <- Symbol.values
      yield Card(number, color, symbol, Shading.SOLID, false)
      Random.shuffle(all.toList)
    else
      val all = for
        number <- 1 to 3
        color <- Color.values
        symbol <- Symbol.values
        shading <- Shading.values
      yield Card(number, color, symbol, shading, false)
      Random.shuffle(all.toList)

  def tableCards(n: Int, table: List[Card], staple: List[Card]): List[Card] =
    if n > table.length then
      table ++ staple.take(n - table.length)
    else
      table.take(n)
    
  def stapleCards(table: List[Card]): List[Card] = allCards.filterNot(card => table.contains(card))

  def selectCards(table: List[Card], card1: Card, card2: Card, card3: Card): List[Card] =
    val unselect = table.map(card => if card.selected then card.toggleSelection else card)
    unselect.map(card => if card == card1 || card == card2 || card == card3 then card.toggleSelection else card)

  def cardAtCoordinate(tableCards: List[Card], coordinate: String, columns: Int): Card =
    val column = coordinate.head
    val row = coordinate.tail.toInt
    val indexColumn = column - 'A'
    val indexRow = row - 1
    tableCards(indexRow * columns + indexColumn)