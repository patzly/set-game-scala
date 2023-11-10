package de.htwg.se.set.view

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.{Card, Deck, Player, Triplet}
import de.htwg.se.set.util.{Event, Observer, PrintUtil}

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Success, Try}

class Tui(controller: Controller) extends Observer:

  controller.add(this)

  def run(): Unit =
    println(controller)
    settingsLoop()

  override def update(e: Event): Unit =
    e match
      case Event.SETTINGS_CHANGED | Event.CARDS_CHANGED =>
        println()
        val tableCards = controller.game.tableCards
        val playerCards = controller.game.playersCards
        val stapleCards = controller.game.deck.stapleCards(tableCards, playerCards)
        println("TableCards: " + tableCards.mkString(", "))
        println("PlayerCards: " + playerCards.mkString(", "))
        println("StapleCards: " + stapleCards.mkString(", "))
        println(controller)
      case _ =>

  @tailrec
  private def settingsLoop(): Unit =
    println(PrintUtil.bold("1") + " Start game")
    println(PrintUtil.bold("2") + " Change number of players")
    println(PrintUtil.bold("3") + " Switch to " + (if controller.settings.easy then "normal" else "easy") + " mode")
    intInput(1, 3) match
      case 1 =>
        controller.setInGame(true)
        controller.setColumns(if controller.settings.easy then 3 else 4)
        controller.setDeck(Deck(controller.settings.easy))
        val deck = controller.game.deck
        val cardsMultiPlayer = deck.tableCards(
          controller.game.rows * controller.game.columns, List[Card](), List[Card]()
        )
        val cardsSinglePlayer = deck.tableCardsSinglePlayer(controller.game.rows * controller.game.columns)
        controller.setTableCards(if controller.settings.singlePlayer then cardsSinglePlayer else cardsMultiPlayer)
        controller.setPlayers((1 to controller.settings.playerCount)
          .map(i => Player(i, controller.settings.singlePlayer, controller.settings.easy, List[Triplet]())).toList)
        gameLoop()
      case 2 =>
        println("Enter number of players:")
        controller.setPlayerCount(intInput(1))
        settingsLoop()
      case 3 =>
        controller.setEasy(!controller.settings.easy)
        settingsLoop()

  private def gameLoop(): Unit =
    if !controller.settings.singlePlayer then
      println(s"Input player who found a SET (e.g. 1) or 0 if no SET can be found:")
    val input = if controller.settings.singlePlayer then 1 else intInput(0, controller.settings.playerCount)
    if input == 0 then
      controller.addColumn()
      val cardsAdded = controller.game.deck.tableCards(
        controller.game.cellCount, controller.game.tableCards, controller.game.playersCards
      )
      if cardsAdded.length > controller.game.tableCards.length then
        println("One column of cards added to the table.")
        controller.setTableCards(cardsAdded)
        gameLoop()
      else if controller.game.deck.findSets(controller.game.tableCards).nonEmpty then
        println(PrintUtil.red("No more cards left, but there still is at least one SET to be found!\n"))
        controller.removeColumn()
        gameLoop()
      else
        gameEnd()
    val player = if controller.settings.singlePlayer then
      controller.game.players.head
    else if input != 0 then
      controller.game.players(input - 1)
    else
      controller.game.players(intInput(1, controller.game.players.length) - 1)

    println(s"Select 3 cards for a SET (e.g. A1 B2 C3):")
    val coordinates = coordinatesInput
    val deck = controller.game.deck
    val cards = deck.tableCards(controller.game.cellCount, controller.game.tableCards, controller.game.playersCards)
    val card1 = deck.cardAtCoordinate(cards, coordinates.head, controller.game.columns)
    val card2 = deck.cardAtCoordinate(cards, coordinates(1), controller.game.columns)
    val card3 = deck.cardAtCoordinate(cards, coordinates(2), controller.game.columns)
    controller.setTableCards(controller.game.deck.selectCards(cards, card1, card2, card3))

    val triplet = Triplet(card1.select, card2.select, card3.select)
    val playerUpdated = player.foundSet(triplet)
    controller.setPlayers(controller.game.players.updated(player.index, playerUpdated))
    val replaceOrRemoveSet = !controller.settings.singlePlayer && triplet.isSet
    controller.setPlayersCards(
      if replaceOrRemoveSet then controller.game.deck.playersCardsAdd(controller.game.playersCards, triplet)
      else controller.game.playersCards
    )
    val stapleEmpty = controller.game.deck.stapleCards(controller.game.tableCards, controller.game.playersCards).isEmpty
    val minColumns = if stapleEmpty then 1 else if controller.game.deck.easy then 3 else 4
    val removeColumn = replaceOrRemoveSet && controller.game.columns > minColumns
    if removeColumn then
      controller.removeColumn()
    else if replaceOrRemoveSet && controller.game.columns == 1 then
      gameEnd()
    controller.setTableCards(
      if replaceOrRemoveSet then
        controller.game.deck.tableCards(
          controller.game.cellCount, controller.game.tableCards, controller.game.playersCards
        )
      else controller.game.deck.unselectCards(controller.game.tableCards)
    )

    if controller.settings.singlePlayer then
      if playerUpdated.sets.nonEmpty then
        println(playerUpdated)
      val finished = if deck.easy then playerUpdated.sets.length == 3 else playerUpdated.sets.length == 6
      if finished then gameEnd()

    gameLoop()

  private def gameEnd(): Unit =
    println("\n" + PrintUtil.yellow(PrintUtil.bold("All SETs found!")))
    if !controller.settings.singlePlayer then
      controller.game.players.sortBy(_.sets.length).reverse.foreach(player => println(player))
    controller.setInGame(false)
    println(controller)
    settingsLoop()
    
  private def stringInput: String = StdIn.readLine().trim

  @tailrec
  private def intInput: Int = Try(stringInput.toInt) match
    case Success(value) => value
    case _ =>
      println(PrintUtil.red("Ungültige Eingabe. Erneut versuchen:"))
      intInput

  @tailrec
  private def intInput(min: Int, max: Int): Int =
    val user = intInput
    if min <= user && user <= max then
      user
    else
      println(PrintUtil.red(s"Nur ganze Zahlen von $min bis $max zulässig. Erneut versuchen:"))
      intInput(min, max)

  @tailrec
  private def intInput(min: Int): Int =
    val user = intInput
    if min <= user then
      user
    else
      println(PrintUtil.red(s"Nur ganze Zahlen ab $min zulässig. Erneut versuchen:"))
      intInput(min)

  @tailrec
  private def coordinatesInput: List[String] =
    val input = stringInput
    val coordinatesPattern = "^([A-Za-z][1-3] +){2}[A-Za-z][1-3]$".r
    input match
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