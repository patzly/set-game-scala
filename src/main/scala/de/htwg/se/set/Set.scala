package de.htwg.se.set

import de.htwg.se.set.view.Tui
import util.PrintUtil
import model.{Card, Deck, Grid, Triplet}

import scala.annotation.tailrec

val rows = 3
val easy = true
val deck = new Deck(easy)

@main
def main(): Unit =
  val columns = if easy then 3 else 4
  val cards = deck.tableCards(rows * columns, List[Card](), deck.allCards)
  println("\n" + new Grid(rows, columns, cards, easy))
  loop(rows, columns, cards, easy)

@tailrec
def loop(rows: Int, columns: Int, tableCards: List[Card], easy: Boolean): Unit =
  println(s"Select 3 cards for a SET (e.g. A1 B2 C3):")
  val coordinates = Tui.coordinatesInput
  val cards = deck.tableCards(rows * columns, tableCards, deck.stapleCards(tableCards))
  val card1 = deck.cardAtCoordinate(cards, coordinates.head, columns)
  val card2 = deck.cardAtCoordinate(cards, coordinates(1), columns)
  val card3 = deck.cardAtCoordinate(cards, coordinates(2), columns)
  val cardsSelected = deck.selectCards(cards, card1, card2, card3)
  println("\n" + new Grid(rows, columns, cardsSelected, easy))
  val triplet = new Triplet(card1, card2, card3)
  println(if triplet.isSet then PrintUtil.green("That's a SET!\n") else PrintUtil.red("That's not a SET!\n"))
  loop(rows, columns, cards, easy)