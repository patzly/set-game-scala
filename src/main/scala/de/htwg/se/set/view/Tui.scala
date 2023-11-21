package de.htwg.se.set.view

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.{Card, ChangePlayerCountCommand, Command, Deck, Player, StartGameCommand, SwitchEasyCommand, Triplet}
import de.htwg.se.set.util.{Event, Observer, PrintUtil}

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Success, Try}

class Tui(controller: Controller) extends Observer:

  controller.add(this)

  def run(): Unit =
    println(controller.settingsToString)
    settingsLoop()

  override def update(event: Event): Unit =
    event match
      case Event.SETTINGS_CHANGED => println(controller.settingsToString)
      case Event.CARDS_CHANGED => println(controller.gameToString)
      case _ =>

  @tailrec
  private def settingsLoop(): Unit =
    println(PrintUtil.bold("1") + " Start game")
    println(PrintUtil.bold("2") + " Change number of players")
    println(PrintUtil.bold("3") + " Switch to " + (if controller.settings.easy then "normal" else "easy") + " mode")
    val command: Command = intInput(1, 3) match
      case 1 => StartGameCommand(controller)
      case 2 =>
        println("Enter number of players:")
        val playerCount = intInput(1, 10)
        ChangePlayerCountCommand(controller, playerCount)
      case 3 => SwitchEasyCommand(controller)
    command.execute()
    if command.isInstanceOf[StartGameCommand] then gameLoop() else settingsLoop()

  @tailrec
  private def gameLoop(): Unit =
    val player = selectPlayerOrAddColumn
    println(s"Select 3 cards for a SET (e.g. A1 B2 C3):")
    val coordinates = coordinatesInput
    val deck = controller.game.deck
    val cards = deck.tableCards(controller.game.columns, controller.game.tableCards, controller.game.playersCards)
    val card1 = deck.cardAtCoordinate(cards, coordinates.head, controller.game.columns)
    val card2 = deck.cardAtCoordinate(cards, coordinates(1), controller.game.columns)
    val card3 = deck.cardAtCoordinate(cards, coordinates(2), controller.game.columns)
    controller.setTableCards(controller.game.deck.selectCards(cards, card1, card2, card3))

    val triplet = Triplet(card1.select, card2.select, card3.select)
    val playerUpdated = player.foundSet(triplet)
    controller.setPlayers(controller.game.players.updated(player.index, playerUpdated))
    val replaceOrRemoveSet = !controller.settings.singlePlayer && triplet.isSet
    controller.setPlayersCards(
      if replaceOrRemoveSet then
        controller.game.deck.playersCardsAdd(controller.game.playersCards, triplet)
      else
        controller.game.playersCards
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
          controller.game.columns, controller.game.tableCards, controller.game.playersCards
        )
      else
        controller.game.deck.unselectCards(controller.game.tableCards)
    )

    if controller.settings.singlePlayer then
      if playerUpdated.sets.nonEmpty then
        println(playerUpdated)
      val finished = if deck.easy then playerUpdated.sets.length == 3 else playerUpdated.sets.length == 6
      if finished then gameEnd()

    gameLoop()

  private def selectPlayerOrAddColumn: Player =
    val singlePlayer = controller.settings.singlePlayer
    if !singlePlayer then
      println(s"Input player who found a SET (e.g. 1) or 0 if no SET can be found:")
    val input = if singlePlayer then 1 else intInput(0, controller.settings.playerCount)
    if input == 0 then
      controller.addColumn()
      val cardsAdded = controller.game.deck.tableCards(
        controller.game.columns, controller.game.tableCards, controller.game.playersCards
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
    if singlePlayer then controller.game.players.head else controller.game.players(input - 1)

  private def gameEnd(): Unit =
    println("\n" + PrintUtil.yellow(PrintUtil.bold("All SETs found!")))
    if !controller.settings.singlePlayer then
      controller.game.players.sortBy(player => (-player.sets.length, player.number)).foreach(player => println(player))
    println(controller.settingsToString)
    settingsLoop()
    
  final def stringInput: String = StdIn.readLine().trim

  @tailrec
  final def intInput: Int = Try(stringInput.toInt) match
    case Success(value) => value
    case _ =>
      println(PrintUtil.red("Invalid input. Try again:"))
      intInput

  @tailrec
  final def intInput(min: Int, max: Int): Int =
    val user = intInput
    if min <= user && user <= max then
      user
    else
      println(PrintUtil.red(s"Only whole numbers from $min to $max allowed. Try again:"))
      intInput(min, max)

  @tailrec
  final def coordinatesInput: List[String] =
    val input = stringInput
    val coordinatesPattern = "^([A-Za-z][1-3] +){2}[A-Za-z][1-3]$".r
    input match
      case coordinatesPattern(_*) =>
        val coordinates = input.split(" +").toSet
        if coordinates.size == 3 then
          coordinates.toList
        else
          println(PrintUtil.red("Only different coordinates possible. Try again:"))
          coordinatesInput
      case _ =>
        println(PrintUtil.red("Invalid input. Try again:"))
        coordinatesInput