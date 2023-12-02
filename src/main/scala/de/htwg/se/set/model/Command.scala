package de.htwg.se.set.model

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.manager.Snapshot
import de.htwg.se.set.util.PrintUtil

sealed trait Command:

  var snapshot: Option[Snapshot] = None
  
  def controller: Controller

  def saveSnapshot(): Unit = snapshot = Some(controller.snapshot)
  
  def undo(): State = snapshot match
    case Some(snapshot) => controller.restoreSnapshot(snapshot)
    case None => throw IllegalStateException("No snapshot to restore")

  def execute: Command

case class StartGameCommand(c: Controller) extends Command:
  
  override def controller: Controller = c
  
  override def execute: StartGameCommand =
    controller.setColumns(if controller.settings.easy then 3 else 4)
    controller.setDeck(Deck(controller.settings.easy))
    val deck = controller.game.deck
    val cardsMultiPlayer = deck.tableCards(controller.game.columns, List[Card](), List[Card]())
    val cardsSinglePlayer = deck.tableCardsSinglePlayer(controller.game.columns)
    controller.setTableCards(if controller.settings.singlePlayer then cardsSinglePlayer else cardsMultiPlayer)
    controller.setPlayers((1 to controller.settings.playerCount)
      .map(i => Player(i, controller.settings.singlePlayer, controller.settings.easy, List[Triplet]())).toList)
    this

case class ChangePlayerCountCommand(c: Controller, playerCount: Int) extends Command:

  override def controller: Controller = c

  override def execute: ChangePlayerCountCommand =
    controller.setPlayerCount(playerCount)
    this

case class SwitchEasyCommand(c: Controller) extends Command:

  override def controller: Controller = c

  override def execute: SwitchEasyCommand =
    controller.setEasy(!controller.settings.easy)
    this

case class SelectPlayerCommand(c: Controller, input: Int) extends Command:

  override def controller: Controller = c

  override def execute: SelectPlayerCommand =
    controller.selectPlayer(input)
    this

case class AddColumnCommand(c: Controller, endGame: Boolean = false) extends Command:

  override def controller: Controller = c

  override def execute: AddColumnCommand =
    controller.addColumn()
    val cardsAdded = controller.game.deck.tableCards(
      controller.game.columns, controller.game.tableCards, controller.game.playersCards
    )
    if cardsAdded.length > controller.game.tableCards.length then
      println("One column of cards added to the table.")
      controller.setTableCards(cardsAdded)
      this
    else if controller.game.deck.findSets(controller.game.tableCards).nonEmpty then
      println(PrintUtil.red("No more cards left, but there still is at least one SET to be found!\n"))
      controller.removeColumn()
      this
    else copy(endGame = true)

case class SelectCardsCommand(c: Controller, coordinates: List[String], endGame: Boolean = false)
  extends Command:

  override def controller: Controller = c

  override def execute: SelectCardsCommand =
    val deck = controller.game.deck
    val cards = deck.tableCards(controller.game.columns, controller.game.tableCards, controller.game.playersCards)
    val card1 = deck.cardAtCoordinate(cards, coordinates.head, controller.game.columns)
    val card2 = deck.cardAtCoordinate(cards, coordinates(1), controller.game.columns)
    val card3 = deck.cardAtCoordinate(cards, coordinates(2), controller.game.columns)
    controller.setTableCards(controller.game.deck.selectCards(cards, card1, card2, card3))

    val triplet = Triplet(card1.select, card2.select, card3.select)
    val player = controller.game.selectedPlayer match
      case Some(p) => p
      case None => throw new IllegalStateException("No player selected")
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
      return copy(endGame = true)
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
      if finished then return copy(endGame = true)
    this