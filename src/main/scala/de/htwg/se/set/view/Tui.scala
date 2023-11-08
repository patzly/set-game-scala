package de.htwg.se.set.view

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.util.{Observer, PrintUtil}

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Success, Try}

class Tui(controller: Controller) extends Observer:

  controller.add(this)

  def run(): Unit = settingsLoop()

  override def update(): Unit = {}

  @tailrec
  private def settingsLoop(): Unit =
    println(controller.settingsToString)
    println(PrintUtil.bold("1") + " Start game")
    println(PrintUtil.bold("2") + " Change number of players")
    println(PrintUtil.bold("3") + " Switch to " + (if controller.settings.easy then "normal" else "easy") + " mode")
    val input = intInput(1, 3)
    if input == 1 then
      println("TODO: Game start")
      /*val rows = 3
      val columns = if easy then 3 else 4
      val deck = Deck(easy)
      val cardsMultiPlayer = deck.tableCards(rows * columns, List[Card](), List[Card]())
      val cardsSinglePlayer = deck.tableCardsSinglePlayer(rows * columns)
      val cards = if playerCount == 1 then cardsSinglePlayer else cardsMultiPlayer
      println("\n" + Grid(rows, columns, cards, easy))
      val players = (1 to playerCount).map(i => Player(i, playerCount == 1, easy, List[Triplet]())).toList
      gameLoop(rows, columns, deck, cards, List[Card](), players)*/
    else if input == 2 then
      println("Enter number of players:")
      controller.setPlayerCount(intInput(1))
      settingsLoop()
    else if input == 3 then
      controller.setEasy(!controller.settings.easy)
      settingsLoop()

  def stringInput: String = StdIn.readLine().trim

  @tailrec
  final def intInput: Int = Try(stringInput.toInt) match {
    case Success(value) => value
    case _ =>
      println(PrintUtil.red("Ungültige Eingabe. Erneut versuchen:"))
      intInput
  }

  @tailrec
  final def intInput(min: Int, max: Int): Int =
    val user = intInput
    if min <= user && user <= max then
      user
    else
      println(PrintUtil.red(s"Nur ganze Zahlen von $min bis $max zulässig. Erneut versuchen:"))
      intInput(min, max)

  @tailrec
  final def intInput(min: Int): Int =
    val user = intInput
    if min <= user then
      user
    else
      println(PrintUtil.red(s"Nur ganze Zahlen ab $min zulässig. Erneut versuchen:"))
      intInput(min)

  @tailrec
  final def coordinatesInput: List[String] =
    val input = stringInput
    val coordinatesPattern = "^([A-Za-z][1-3] +){2}[A-Za-z][1-3]$".r
    input match {
      case coordinatesPattern(_*) =>
        val coordinates = input.split(" +").toSet
        if coordinates.size == 3 then
          coordinates.toList
        else
          println(PrintUtil.red("Nur verschiedene Koordinaten möglich. Erneut versuchen:"))
          coordinatesInput
      case _ =>
        println(PrintUtil.red(s"Ungültige Eingabe. Erneut versuchen:"))
        coordinatesInput
    }