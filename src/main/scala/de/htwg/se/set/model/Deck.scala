package de.htwg.se.set.model

import scala.util.Random

case class Deck(easy: Boolean):

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

  def findSets(cards: List[Card]): List[Triplet] =
    val sets = for {
      card1 <- cards
      card2 <- cards if card2 != card1
      card3 <- cards if card3 != card1 && card3 != card2
      triplet = Triplet(card1, card2, card3) if triplet.isSet
    } yield triplet
    sets.toSet.toList

  def tableCards(n: Int, table: List[Card], players: List[Card]): List[Card] =
    if n < table.length then
      table.filterNot(card => players.contains(card)).take(n)
    else
      val staple = stapleCards(table, players)
      val stapleIterator = staple.iterator
      val tableUpdated = table.map {
        case card if players.contains(card) => if stapleIterator.hasNext then stapleIterator.next() else card
        case card => card
      }
      val tableRemoved = tableUpdated.filterNot(card => players.contains(card))
      if n > tableRemoved.length then
        val staple = stapleCards(table, players)
        tableRemoved ++ staple.take(n - table.length)
      else
        tableRemoved.take(n)

  def tableCardsSinglePlayer(n: Int): List[Card] =
    val allSets = findSets(allCards)
    val selectedSets = Random.shuffle(allSets).take(if easy then 3 else 6)
    val cards = selectedSets.flatMap(triplet => List(triplet.card1, triplet.card2, triplet.card3)).distinct
    if n > cards.length then
      val filledUp = cards ++ allCards.filterNot(card => cards.contains(card)).take(n - cards.length)
      Random.shuffle(filledUp)
    else
      Random.shuffle(cards.take(n))
  
  private def stapleCards(table: List[Card], players: List[Card]): List[Card] =
    allCards.filterNot(card => table.contains(card) || players.contains(card))

  def playersCardsAdd(playersCards: List[Card], set: Triplet): List[Card] =
    playersCards ++ List(set.card1, set.card2, set.card3)

  def selectCards(table: List[Card], card1: Card, card2: Card, card3: Card): List[Card] =
    val unselect = table.map(card => if card.selected then card.unselect else card)
    unselect.map(card => if card == card1 || card == card2 || card == card3 then card.select else card)
    
  def unselectCards(table: List[Card]): List[Card] =
    table.map(card => if card.selected then card.unselect else card)

  def cardAtCoordinate(tableCards: List[Card], coordinate: String, columns: Int): Card =
    val column = coordinate.head.toUpper - 'A'
    val row = coordinate.tail.toInt - 1
    val index = column * tableCards.length / columns + row
    tableCards(index)