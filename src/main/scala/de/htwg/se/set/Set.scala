package de.htwg.se.set

import util.PrintUtil
import model.{Card, Color, Grid, Shading, Symbol, TextInput, Triplet}

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.Random

val rows = 3
val easy = true
val allCards: List[Card] =
  if easy then
    val all = for
      number <- 1 to 3
      color <- Color.values
      symbol <- Symbol.values
    yield Card(number, color, symbol, Shading.SOLID)
    Random.shuffle(all.toList)
  else
    val all = for
      number <- 1 to 3
      color <- Color.values
      symbol <- Symbol.values
      shading <- Shading.values
    yield Card(number, color, symbol, shading)
    Random.shuffle(all.toList)

@main
def main(): Unit = loop(rows, 4, "", easy)

@tailrec
def loop(rows: Int, columns: Int, input: String, easy: Boolean): Unit =
  val cards = allCards.take(rows * columns)
  var selected = List.fill(rows * columns)(false)
  val textInput = new TextInput(input)
  if textInput.nonEmpty then
    if textInput.hasCoordinates then
      val coordinates = textInput.coordinates
      coordinates.foreach: coordinate =>
        val index = indexFromCoordinate(coordinate)
        selected = selected.updated(index, true)
      val grid = new Grid(rows, columns, cards, selected, easy)
      println("\n" + grid)
      val card1 = cards(indexFromCoordinate(coordinates.head))
      val card2 = cards(indexFromCoordinate(coordinates(1)))
      val card3 = cards(indexFromCoordinate(coordinates(2)))
      val triplet = new Triplet(card1, card2, card3)
      if triplet.isSet then
        println(PrintUtil.green("That's a SET!\n"))
      else
        println(PrintUtil.red("That's not a SET!\n"))
    else
      println(PrintUtil.red("Invalid input!\n"))
  else
    val grid = new Grid(rows, columns, cards, selected, easy)
    println("\n" + grid)
  println(s"Select 3 cards for a SET (e.g. A1 B2 C3):")
  loop(rows, columns, StdIn.readLine, easy)

def indexFromCoordinate(coordinate: String): Int =
  val column = coordinate.head
  val row = coordinate.tail.toInt
  val columnIdx = column - 'A'
  val rowIdx = row - 1
  val numberOfColumns = 4
  rowIdx * numberOfColumns + columnIdx