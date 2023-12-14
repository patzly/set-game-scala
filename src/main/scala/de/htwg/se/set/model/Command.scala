package de.htwg.se.set.model

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.manager.Snapshot
import de.htwg.se.set.model.GameMode.{GAME_END, IN_GAME, SETTINGS}
import de.htwg.se.set.util.PrintUtil

sealed trait Command(controller: Controller):

  var snapshot: Option[Snapshot] = None

  def saveSnapshot(): Unit = snapshot = Some(controller.snapshot)
  
  def undo(): Unit = snapshot match
    case Some(snapshot) => controller.restoreSnapshot(snapshot)
    case None => throw IllegalStateException("No snapshot to restore")

  def execute(): Unit

case class StartGameCommand(controller: Controller) extends Command(controller):
  
  override def execute(): Unit =
    controller.setColumns(if controller.settings.easy then 3 else 4)
    controller.setDeck(Deck(controller.settings.easy))
    val deck = controller.game.deck
    val cardsMultiPlayer = deck.tableCards(controller.game.columns, List[Card](), List[Card]())
    val cardsSinglePlayer = deck.tableCardsSinglePlayer(controller.game.columns)
    val singlePlayer = controller.settings.singlePlayer
    controller.setTableCards(if singlePlayer then cardsSinglePlayer else cardsMultiPlayer)
    controller.setPlayersCards(List())
    controller.setPlayers((1 to controller.settings.playerCount)
      .map(i => Player(i, singlePlayer, controller.settings.easy, List[Triplet]())).toList)
    if singlePlayer then controller.selectPlayer(1)
    controller.setGameMode(IN_GAME)
    val state = if singlePlayer then GameState(controller) else SelectPlayerState(controller)
    controller.setMessage(state.message)
    controller.changeState(state)

case class GoToPlayerCountCommand(controller: Controller) extends Command(controller):

  override def execute(): Unit = controller.changeState(ChangePlayerCountState(controller))

case class ChangePlayerCountCommand(controller: Controller, playerCount: Int) extends Command(controller):

  override def execute(): Unit =
    controller.setPlayerCount(playerCount)
    controller.changeState(SettingsState(controller))

case class SwitchEasyCommand(controller: Controller) extends Command(controller):

  override def execute(): Unit =
    controller.setEasy(!controller.settings.easy)
    controller.changeState(SettingsState(controller))

case class SelectPlayerCommand(controller: Controller, number: Int) extends Command(controller):

  override def execute(): Unit =
    controller.selectPlayer(number)
    val state = GameState(controller)
    controller.setMessage(state.message)
    controller.changeState(state)

case class AddColumnCommand(controller: Controller) extends Command(controller):

  override def execute(): Unit =
    controller.addColumn()
    val cardsAdded = controller.game.deck.tableCards(
      controller.game.columns, controller.game.tableCards, controller.game.playersCards
    )
    if cardsAdded.length > controller.game.tableCards.length then
      val msg = "One column of cards added to the table."
      println(msg)
      controller.setMessage(msg)
      controller.setTableCards(cardsAdded)
      controller.changeState(SelectPlayerState(controller))
    else if controller.game.deck.findSets(controller.game.tableCards).nonEmpty then
      val msg = "No more cards left, but there still is at least one SET to be found!"
      println(PrintUtil.red(msg + "\n"))
      controller.setMessage(msg)
      controller.removeColumn()
      controller.changeState(SelectPlayerState(controller))
    else
      controller.setGameMode(GAME_END)
      controller.changeState(GameEndState(controller))

case class SelectCardsCommand(controller: Controller, coordinates: List[String]) extends Command(controller):

  override def execute(): Unit =
    val deck = controller.game.deck
    val cards = deck.tableCards(controller.game.columns, controller.game.tableCards, controller.game.playersCards)
    val card1 = deck.cardAtCoordinate(cards, coordinates.head, controller.game.columns)
    val card2 = deck.cardAtCoordinate(cards, coordinates(1), controller.game.columns)
    val card3 = deck.cardAtCoordinate(cards, coordinates(2), controller.game.columns)
    controller.setTableCards(controller.game.deck.selectCards(cards, card1, card2, card3))

    val triplet = Triplet(card1.select, card2.select, card3.select)
    val player = controller.game.selectedPlayer match
      case Some(p) => p
      case None => throw IllegalStateException("No player selected")
    val playerUpdated = if player.sets.contains(triplet) then
      val msg = "SET already found."
      println(PrintUtil.red(msg))
      controller.setMessage(msg)
      player
    else if triplet.isSet then
      val msg = "That's a SET!"
      println(PrintUtil.green(msg))
      controller.setMessage(msg)
      player.copy(sets = player.sets.appended(triplet))
    else if player.sets.isEmpty || controller.settings.singlePlayer then
      val msg = "That's not a SET!"
      println(PrintUtil.red(msg))
      controller.setMessage(msg)
      player
    else
      val msg = "That's not a SET! One SET removed."
      println(PrintUtil.red(msg))
      controller.setMessage(msg)
      player.copy(sets = player.sets.dropRight(1))
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
      controller.setGameMode(GAME_END)
      controller.changeState(GameEndState(controller))
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
      if finished then
        controller.setGameMode(GAME_END)
        controller.changeState(GameEndState(controller))
      else
        controller.selectPlayer(1)
        controller.changeState(GameState(controller))
    else
      controller.changeState(SelectPlayerState(controller))

case class ExitCommand(controller: Controller) extends Command(controller):

  override def execute(): Unit =
    controller.setGameMode(SETTINGS)
    println(controller.settingsToString)
    controller.changeState(SettingsState(controller))