package de.htwg.se.set.controller.controller.base

import de.htwg.se.set.controller.{ICommand, IController}
import de.htwg.se.set.model.GameMode.{GAME_END, IN_GAME, SETTINGS}
import de.htwg.se.set.model.game.base.{Deck, Player, Triplet}
import de.htwg.se.set.model.{ICard, ITriplet}
import de.htwg.se.set.util.PrintUtil
import play.api.libs.json.{JsValue, Json}

import scala.xml.{Node, Utility}

private class Command(controller: IController) extends ICommand(controller):

  private var snapshot: Option[Snapshot] = None

  def saveSnapshot(): Unit = snapshot = Some(controller.snapshot)

  def undo(): Unit = snapshot match
    case Some(snapshot) => controller.restoreSnapshot(snapshot)
    case None => throw IllegalStateException("No snapshot to restore")

  override def execute(): Unit = ()

case class StartGameCommand(controller: IController) extends Command(controller):
  
  override def execute(): Unit =
    controller.setColumns(if controller.settings.easy then 3 else 4)
    controller.setDeck(Deck(controller.settings.easy))
    val deck = controller.game.deck
    val cardsMultiPlayer = deck.tableCards(controller.game.columns, List[ICard](), List[ICard]())
    val cardsSinglePlayer = deck.tableCardsSinglePlayer(controller.game.columns)
    val singlePlayer = controller.settings.singlePlayer
    controller.setTableCards(if singlePlayer then cardsSinglePlayer else cardsMultiPlayer)
    controller.setPlayersCards(List())
    controller.setPlayers((1 to controller.settings.playerCount)
      .map(i => Player(i, singlePlayer, controller.settings.easy, List[ITriplet]())).toList)
    if singlePlayer then controller.selectPlayer(1) else controller.unselectPlayer()
    controller.setGameMode(IN_GAME)
    val state = if singlePlayer then GameState(controller) else SelectPlayerState(controller)
    controller.setMessage(state.message)
    controller.changeState(state)

case class GoToPlayerCountCommand(controller: IController) extends Command(controller):

  override def execute(): Unit = controller.changeState(ChangePlayerCountState(controller))

case class ChangePlayerCountCommand(controller: IController, playerCount: Int) extends Command(controller):

  override def execute(): Unit =
    controller.setPlayerCount(playerCount)
    controller.changeState(SettingsState(controller))

case class SwitchEasyCommand(controller: IController) extends Command(controller):

  override def execute(): Unit =
    controller.setEasy(!controller.settings.easy)
    controller.changeState(SettingsState(controller))

case class SelectPlayerCommand(controller: IController, number: Int) extends Command(controller):

  override def execute(): Unit =
    controller.selectPlayer(number)
    val state = GameState(controller)
    controller.setMessage(state.message)
    controller.changeState(state)

case class AddColumnCommand(controller: IController) extends Command(controller):

  override def execute(): Unit =
    controller.addColumn()
    val cardsAdded = controller.game.deck.tableCards(
      controller.game.columns, controller.game.tableCards, controller.game.playersCards
    )
    val cardsAvailable = cardsAdded.length > controller.game.tableCards.length
    val setsAvailable = controller.game.deck.findSets(controller.game.tableCards).nonEmpty
    if cardsAvailable && controller.game.columns <= 6 then
      val msg = "One column of cards added to the table."
      println(msg)
      controller.setMessage(msg)
      controller.setTableCards(cardsAdded)
      controller.changeState(SelectPlayerState(controller))
    else if setsAvailable then
      val msg = if cardsAvailable && controller.game.columns > 6 then
        "Maximum number of columns reached, but there still is at least one SET to be found!"
      else
        "No more cards left, but there still is at least one SET to be found!"
      println(PrintUtil.red(msg + "\n"))
      controller.setMessage(msg)
      controller.removeColumn()
      controller.changeState(SelectPlayerState(controller))
    else
      controller.setGameMode(GAME_END)
      controller.changeState(GameEndState(controller))

case class SelectCardsCommand(controller: IController, coordinates: List[String]) extends Command(controller):

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
      player.setSets(player.sets.appended(triplet))
    else if player.sets.isEmpty || controller.settings.singlePlayer then
      val msg = "That's not a SET!"
      println(PrintUtil.red(msg))
      controller.setMessage(msg)
      player
    else
      val msg = "That's not a SET! One SET removed."
      println(PrintUtil.red(msg))
      controller.setMessage(msg)
      player.setSets(player.sets.dropRight(1))
    controller.updatePlayer(playerUpdated)
    controller.unselectPlayer()
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

case class ExitCommand(controller: IController) extends Command(controller):

  override def execute(): Unit =
    controller.setGameMode(SETTINGS)
    println(controller.settingsToString)
    controller.changeState(SettingsState(controller))

case class LoadXmlCommand(controller: IController, node: Node) extends Command(controller):

  override def execute(): Unit =
    val hash = (node \ "hash").text
    val xmlSnapshot = (node \ "snapshot").head
    if hash == Snapshot.hash(Utility.trim(xmlSnapshot).toString) then
      controller.restoreSnapshot(Snapshot.fromXml(xmlSnapshot, controller))
    else
      println(PrintUtil.red("Invalid XML progress file!"))

case class LoadJsonCommand(controller: IController, json: JsValue) extends Command(controller):

  override def execute(): Unit =
    val hash = (json \ "hash").get.as[String]
    val jsonSnapshot = (json \ "snapshot").get
    if hash == Snapshot.hash(Json.stringify(jsonSnapshot)) then
      controller.restoreSnapshot(Snapshot.fromJson(jsonSnapshot, controller))
    else
      println(PrintUtil.red("Invalid JSON progress file!"))