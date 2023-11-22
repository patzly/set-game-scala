package de.htwg.se.set.model

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.util.PrintUtil
import de.htwg.se.set.view.Tui

sealed trait TuiState(tui: Tui):

  def controller: Controller = tui.controller

  def run(): Unit

case class SettingsState(tui: Tui) extends TuiState(tui):

  override def run(): Unit =
    println(PrintUtil.bold("1") + " Start game")
    println(PrintUtil.bold("2") + " Change number of players")
    println(PrintUtil.bold("3") + " Switch to " + (if controller.settings.easy then "normal" else "easy") + " mode")
    tui.intInput(1, 3) match
      case 1 =>
        StartGameCommand(controller).execute()
        tui.changeState(SelectPlayerState(tui))
      case 2 =>
        tui.changeState(ChangePlayerCountState(tui))
      case 3 =>
        SwitchEasyCommand(controller).execute()

case class ChangePlayerCountState(tui: Tui) extends TuiState(tui):

  override def run(): Unit =
    println("Enter number of players:")
    val playerCount = tui.intInput(1, 10)
    ChangePlayerCountCommand(controller, playerCount).execute()
    tui.changeState(SettingsState(tui))

case class SelectPlayerState(tui: Tui) extends TuiState(tui):

  override def run(): Unit =
    if !controller.settings.singlePlayer then
      println(s"Input player who found a SET (e.g. 1) or 0 if no SET can be found:")
    val input = if controller.settings.singlePlayer then 1 else tui.intInput(0, controller.settings.playerCount)
    if input != 0 then
      controller.selectPlayer(input)
    else
      controller.addColumn()
      val cardsAdded = controller.game.deck.tableCards(
        controller.game.columns, controller.game.tableCards, controller.game.playersCards
      )
      if cardsAdded.length > controller.game.tableCards.length then
        println("One column of cards added to the table.")
        controller.setTableCards(cardsAdded)
      else if controller.game.deck.findSets(controller.game.tableCards).nonEmpty then
        println(PrintUtil.red("No more cards left, but there still is at least one SET to be found!\n"))
        controller.removeColumn()
      else
        tui.changeState(GameEndState(tui))
    tui.changeState(GameState(tui))

case class GameState(tui: Tui) extends TuiState(tui):

  override def run(): Unit =
    val selectedPlayer = controller.game.selectedPlayer
    val player = selectedPlayer match
      case Some(p) => p
      case None =>
        tui.changeState(SelectPlayerState(tui))
        return;
    println(s"Select 3 cards for a SET (e.g. A1 B2 C3):")
    val coordinates = tui.coordinatesInput
    val deck = controller.game.deck
    val cards = deck.tableCards(controller.game.columns, controller.game.tableCards, controller.game.playersCards)
    val card1 = deck.cardAtCoordinate(cards, coordinates.head, controller.game.columns)
    val card2 = deck.cardAtCoordinate(cards, coordinates(1), controller.game.columns)
    val card3 = deck.cardAtCoordinate(cards, coordinates(2), controller.game.columns)
    controller.setTableCards(controller.game.deck.selectCards(cards, card1, card2, card3))

    val triplet = Triplet(card1.select, card2.select, card3.select)
    val playerUpdated = player.foundSet(triplet)
    controller.updateAndUnselectPlayer(playerUpdated)
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
      tui.changeState(GameEndState(tui))
      return;
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
      if finished then tui.changeState(GameEndState(tui))

case class GameEndState(tui: Tui) extends TuiState(tui):

  override def run(): Unit =
    println("\n" + PrintUtil.yellow(PrintUtil.bold("All SETs found!")))
    if !controller.settings.singlePlayer then
      controller.game.players.sortBy(player => (-player.sets.length, player.number)).foreach(player => println(player))
    println(controller.settingsToString)
    tui.changeState(SettingsState(tui))