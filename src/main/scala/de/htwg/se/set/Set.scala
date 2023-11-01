package de.htwg.se.set

import util.PrintUtil
import model.{Card, Color, Deck, Grid, Shading, Symbol, TextInput, Triplet}

import scala.annotation.tailrec
import scala.io.StdIn

val rows = 3
val easy = true
val deck = new Deck(easy)

@main
def main(): Unit =
  val columns = if easy then 3 else 4
  val tableCards = deck.tableCards(rows * columns, List[Card](), deck.allCards)
  loop(rows, columns, tableCards, "", easy)

@tailrec
def loop(rows: Int, columns: Int, tableCards: List[Card], input: String, easy: Boolean): Unit =
  val cards = deck.tableCards(rows * columns, tableCards, deck.stapleCards(tableCards))
  val textInput = new TextInput(input)
  if textInput.nonEmpty then
    if textInput.hasCoordinates then
      val coordinates = textInput.coordinates
      val card1 = deck.cardAtCoordinate(cards, coordinates.head, columns)
      val card2 = deck.cardAtCoordinate(cards, coordinates(1), columns)
      val card3 = deck.cardAtCoordinate(cards, coordinates(2), columns)
      val cardsSelected = deck.selectCards(cards, card1, card2, card3)

      val grid = new Grid(rows, columns, cardsSelected, easy)
      println("\n" + grid)

      val triplet = new Triplet(card1, card2, card3)
      if triplet.isSet then
        println(PrintUtil.green("That's a SET!\n"))
      else
        println(PrintUtil.red("That's not a SET!\n"))
    else
      println(PrintUtil.red("Invalid input!\n"))
  else
    val grid = new Grid(rows, columns, cards, easy)
    println("\n" + grid)
  println(s"Select 3 cards for a SET (e.g. A1 B2 C3):")
  loop(rows, columns, cards, StdIn.readLine, easy)