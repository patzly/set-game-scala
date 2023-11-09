package de.htwg.se.set.view

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.{Card, Deck, Player, Triplet}
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
      val playerCount = controller.settings.playerCount
      val easy = controller.settings.easy
      val rows = 3
      val columns = if easy then 3 else 4
      val deck = Deck(easy)
      val cardsMultiPlayer = deck.tableCards(rows * columns, List[Card](), List[Card]())
      val cardsSinglePlayer = deck.tableCardsSinglePlayer(rows * columns)
      controller.setColumns(columns)
      controller.setDeck(deck)
      controller.setTableCards(if playerCount == 1 then cardsSinglePlayer else cardsMultiPlayer)
      controller.setPlayers((1 to playerCount).map(i => Player(i, playerCount == 1, easy, List[Triplet]())).toList)
      gameLoop()
    else if input == 2 then
      println("Enter number of players:")
      controller.setPlayerCount(intInput(1))
      settingsLoop()
    else if input == 3 then
      controller.setEasy(!controller.settings.easy)
      settingsLoop()

  private def gameLoop(): Unit =
    println(controller.gameToString)
    
    val players = controller.game.players
    val singlePlayer = controller.settings.singlePlayer
    val rows = controller.game.rows
    val columns = controller.game.columns
    val deck = controller.game.deck
    val tableCards = controller.game.tableCards
    val playersCards = controller.game.playersCards
    
    if !singlePlayer then
      println(s"Input player who found a SET (e.g. 1) or 0 if no SET can be found:")
    val input = if singlePlayer then 1 else intInput(0, controller.settings.playerCount)
    if input == 0 then
      val columnsUpdated = columns + 1
      val cardsAdded = deck.tableCards(rows * columnsUpdated, tableCards, playersCards)
      if cardsAdded.length > tableCards.length then
        println("One column of cards added to the table.")
        controller.setColumns(columnsUpdated)
        controller.setTableCards(cardsAdded)
        gameLoop()
      else if deck.findSets(tableCards).nonEmpty then
        println(PrintUtil.red("No more cards left, but there still is at least one SET to be found!\n"))
        gameLoop()
      else
        println("\n" + PrintUtil.yellow(PrintUtil.bold("All SETs found!")))
        players.foreach(player => println(player))
        settingsLoop()
    val player = if singlePlayer then players.head
    else if input != 0 then players(input - 1)
    else players(intInput(1, players.length) - 1)

    println(s"Select 3 cards for a SET (e.g. A1 B2 C3):")
    val coordinates = coordinatesInput
    val cards = deck.tableCards(rows * columns, tableCards, playersCards)
    val card1 = deck.cardAtCoordinate(cards, coordinates.head, columns)
    val card2 = deck.cardAtCoordinate(cards, coordinates(1), columns)
    val card3 = deck.cardAtCoordinate(cards, coordinates(2), columns)
    controller.setTableCards(deck.selectCards(cards, card1, card2, card3))
    println(controller.gameToString)

    val triplet = Triplet(card1.select, card2.select, card3.select)
    val playerUpdated = player.foundSet(triplet)
    val replaceSet = !singlePlayer && triplet.isSet
    controller.setPlayersCards(if replaceSet then deck.playersCardsAdd(playersCards, triplet) else playersCards)
    controller.setColumns(if replaceSet && columns > (if deck.easy then 3 else 4) then columns - 1 else columns)
    // TODO: remove min columns condition and replace with isStapleEmpty condition
    controller.setTableCards(
      if replaceSet then 
        deck.tableCards(rows * controller.game.columns, controller.game.tableCards, controller.game.playersCards)
      else
        deck.unselectCards(controller.game.tableCards)
    )

    if singlePlayer && playerUpdated.sets.nonEmpty then
      println(playerUpdated)
    val playersUpdated = players.updated(player.index, playerUpdated)

    if singlePlayer then
      val finished = if deck.easy then playerUpdated.sets.length == 3 else playerUpdated.sets.length == 6
      if finished then
        println("\n" + PrintUtil.yellow(PrintUtil.bold("All SETs found!")))
        settingsLoop()

    gameLoop()
    
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