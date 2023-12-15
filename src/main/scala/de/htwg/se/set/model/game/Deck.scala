package de.htwg.se.set.model.game

import de.htwg.se.set.model.{Color, ICard, IDeck, ITriplet, Shading, Symbol}

import scala.util.Random

case class Deck(easy: Boolean) extends IDeck:

  val allCards: List[ICard] = (if easy then new EasyCreationStrategy else new NormalCreationStrategy).create

  override def findSets(cards: List[ICard]): List[ITriplet] =
    val sets = for {
      card1 <- cards
      card2 <- cards if card2 != card1
      card3 <- cards if card3 != card1 && card3 != card2
      triplet = Triplet(card1, card2, card3) if triplet.isSet
    } yield triplet
    sets.toSet.toList

  override def tableCards(columns: Int, table: List[ICard], players: List[ICard]): List[ICard] =
    val n = columns * 3
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

  override def tableCardsSinglePlayer(columns: Int): List[ICard] =
    val n = columns * 3
    val allSets = findSets(allCards)
    val selectedSets = Random.shuffle(allSets).take(if easy then 3 else 6)
    val cards = selectedSets.flatMap(triplet => List(triplet.card1, triplet.card2, triplet.card3)).distinct
    if n > cards.length then
      val filledUp = cards ++ allCards.filterNot(card => cards.contains(card)).take(n - cards.length)
      Random.shuffle(filledUp)
    else
      Random.shuffle(cards.take(n))

  override def stapleCards(table: List[ICard], players: List[ICard]): List[ICard] =
    allCards.filterNot(card => table.contains(card) || players.contains(card))

  override def playersCardsAdd(playersCards: List[ICard], set: ITriplet): List[ICard] =
    playersCards ++ List(set.card1, set.card2, set.card3)

  override def selectCards(table: List[ICard], card1: ICard, card2: ICard, card3: ICard): List[ICard] =
    val unselect = table.map(card => if card.selected then card.unselect else card)
    unselect.map(card => if card == card1 || card == card2 || card == card3 then card.select else card)

  override def unselectCards(table: List[ICard]): List[ICard] =
    table.map(card => if card.selected then card.unselect else card)

  override def cardAtCoordinate(tableCards: List[ICard], coordinate: String, columns: Int): ICard =
    val column = coordinate.head.toUpper - 'A'
    val row = coordinate.tail.toInt - 1
    val index = column * tableCards.length / columns + row
    tableCards(index)

  private trait CreationStrategy:

    def create: List[ICard]

  private class NormalCreationStrategy extends CreationStrategy:

    override def create: List[ICard] =
      val all = for
        number <- 1 to 3
        color <- Color.values
        symbol <- Symbol.values
        shading <- Shading.values
      yield Card(number, color, symbol, shading)
      Random.shuffle(all.toList)

  private class EasyCreationStrategy extends CreationStrategy:

    override def create: List[ICard] =
      val all = for
        number <- 1 to 3
        color <- Color.values
        symbol <- Symbol.values
      yield Card(number, color, symbol)
      Random.shuffle(all.toList)