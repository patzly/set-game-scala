package de.htwg.se.set

import model.{Card, Color, Grid, Shading, Symbol, TextInput}

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.Random

@main
def main(): Unit =
  val rows = 3
  val columns = 4
  val easy = false
  println("Welcome to the SET Game!")
  loop(rows, columns, List[String](), easy)

@tailrec
def loop(rows: Int, columns: Int, coordinates: List[String], easy: Boolean): Unit =
  val cards = randomCards(rows * columns, easy)
  var selected = List.fill(rows * columns)(false)
  if coordinates.nonEmpty then
    coordinates.foreach { coord =>
      val idx = getIndexFromStr(coord)
      selected = selected.updated(idx, true) // Setzt den Wert an der berechneten Indexposition auf 'true'
    }
  val grid = new Grid(rows, columns, cards, selected, easy)
  println("\n" + grid)

  if coordinates.length == 3 then
    val idx1 = getIndexFromStr(coordinates.head)
    val idx2 = getIndexFromStr(coordinates(1))
    val idx3 = getIndexFromStr(coordinates(2))
    val card1 = cards(idx1)
    val card2 = cards(idx2)
    val card3 = cards(idx3)
    if isSet(card1, card2, card3) then
      println("That's a SET!\n")
    else
      println("That's not a SET!\n")
  val remaining = if coordinates.length == 3 then 3 else 3 - coordinates.length
  println(s"Select $remaining cards for a SET (e.g. A1 B2 C3):")
  if coordinates.nonEmpty && coordinates.length < 3 then
    println(coordinates.mkString(" "))
  val userInput = new TextInput(StdIn.readLine())
  if userInput.hasCoordinates then
    val coordinates = userInput.coordinates
    loop(rows, columns, userInput.coordinates, easy)
  //if (userInput.toLowerCase == "ja")

def getIndex(column: Char, row: Int, columns: Int): Int =
  val columnIdx = column - 'A'
  val rowIdx = row - 1
  val numberOfColumns = 4
  rowIdx * numberOfColumns + columnIdx

def getIndexFromStr(s: String): Int =
  val column = s.head // Der Buchstabe am Anfang des Strings
  val row = s.tail.toInt // Die verbleibenden Zahlen im String
  getIndex(column, row, 4)

def isSet(card1: Card, card2: Card, card3: Card): Boolean =
  true

def allCards(easy: Boolean): List[Card] =
  if easy then
    val all = for
      number <- 1 to 3
      color <- Color.values
      symbol <- Symbol.values
    yield new Card(number, color, symbol, Shading.SOLID)
    all.toList
  else
    val all = for
      number <- 1 to 3
      color <- Color.values
      symbol <- Symbol.values
      shading <- Shading.values
    yield new Card(number, color, symbol, shading)
    all.toList

def randomCards(n: Int, easy: Boolean): List[Card] =
  Random.shuffle(allCards(easy)).take(n)

def randomBooleans(n: Int): List[Boolean] =
  val random = new Random
  List.fill(n)(random.nextBoolean())