package de.htwg.se.set.controller.controller

import com.google.inject.Inject
import de.htwg.se.set.controller.{Event, IAction, IController, IState}
import de.htwg.se.set.model.*
import play.api.libs.json.Json

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.xml.{Elem, PrettyPrinter, Utility, XML}

case class Controller @Inject() (var settings: ISettings, var game: IGame) extends IController:

  private val undoManager = new UndoManager
  private var state: IState = SettingsState(this)

  override def currentState: String = state.toString

  override def changeState(s: IState): Unit =
    state = s
    notifyObservers(Event.STATE_CHANGED)

  override def actionFromInput(input: String): IAction = state.actionFromInput(input)

  override def handleAction(action: IAction): Unit =
    action match
      case StartGameAction() => undoManager.executeCommand(StartGameCommand(this))
      case GoToPlayerCountAction() => undoManager.executeCommand(GoToPlayerCountCommand(this))
      case SwitchEasyAction() => undoManager.executeCommand(SwitchEasyCommand(this))
      case ChangePlayerCountAction(playerCount) => undoManager.executeCommand(ChangePlayerCountCommand(this, playerCount))
      case SelectPlayerAction(number) => undoManager.executeCommand(SelectPlayerCommand(this, number))
      case AddColumnAction() => undoManager.executeCommand(AddColumnCommand(this))
      case SelectCardsAction(coordinates) => undoManager.executeCommand(SelectCardsCommand(this, coordinates))
      case ExitAction() => undoManager.executeCommand(ExitCommand(this))
      case LoadXmlAction(node) => undoManager.executeCommand(LoadXmlCommand(this, node))
      case LoadJsonAction(json) => undoManager.executeCommand(LoadJsonCommand(this, json))
      case UndoAction() => undoManager.undoCommand()
      case RedoAction() => undoManager.redoCommand()
      case _ =>

  override def snapshot: Snapshot = Snapshot(settings, game, state)

  override def restoreSnapshot(snapshot: Snapshot): Unit =
    settings = snapshot.settings
    game = snapshot.game
    state = snapshot.state
    notifyObservers(Event.SETTINGS_OR_GAME_CHANGED)
    notifyObservers(Event.STATE_CHANGED)
    notifyObservers(Event.GAME_MODE_CHANGED)

  override def save(): Unit =
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")
    val date = formatter.format(LocalDateTime.now)
    val name = s"progress_$date"
    saveXml(name)
    saveJson(name)

  private def saveXml(name: String): Unit =
    val xmlSnapshot = snapshot.toXml
    val hash = Snapshot.hash(Utility.trim(XML.loadString(xmlSnapshot.toString)).toString)
    val xml =
      <progress>
        <hash>{hash}</hash>
        {xmlSnapshot}
      </progress>
    XML.save(name + ".xml", XML.loadString(PrettyPrinter(100, 2).format(xml)), "UTF-8", true, null)

  private def saveJson(name: String): Unit =
    val jsonSnapshot = snapshot.toJson
    val hash = Snapshot.hash(Json.stringify(jsonSnapshot))
    val json = Json.obj(
      "hash" -> hash,
      "snapshot" -> jsonSnapshot
    )
    Files.write(Paths.get(name + ".json"), Json.prettyPrint(json).getBytes(StandardCharsets.UTF_8))

  override def canUndo: Boolean = undoManager.canUndo

  override def canRedo: Boolean = undoManager.canRedo

  override def setPlayerCount(count: Int): Unit =
    settings = settings.setPlayerCount(count)
    notifyObservers(Event.SETTINGS_CHANGED)

  override def setEasy(easy: Boolean): Unit =
    settings = settings.setEasy(easy)
    notifyObservers(Event.SETTINGS_CHANGED)

  override def setGameMode(mode: GameMode): Unit =
    settings = settings.setGameMode(mode)
    notifyObservers(Event.GAME_MODE_CHANGED)

  override def setColumns(columns: Int): Unit =
    game = game.setColumns(columns)
    notifyObservers(Event.COLUMNS_CHANGED)

  override def addColumn(): Unit =
    game = game.setColumns(game.columns + 1)
    notifyObservers(Event.COLUMNS_CHANGED)

  override def removeColumn(): Unit =
    game = game.setColumns(game.columns - 1 max 1)
    notifyObservers(Event.COLUMNS_CHANGED)

  override def setDeck(deck: IDeck): Unit = game = game.setDeck(deck)

  override def setTableCards(cards: List[ICard]): Unit =
    game = game.setTableCards(cards)
    notifyObservers(Event.CARDS_CHANGED)

  override def setPlayersCards(cards: List[ICard]): Unit = game = game.setPlayersCards(cards)

  override def setPlayers(players: List[IPlayer]): Unit =
    game = game.setPlayers(players)
    notifyObservers(Event.PLAYERS_CHANGED)

  override def updatePlayer(player: IPlayer): Unit =
    game = game.setPlayers(game.players.updated(player.index, player))
    notifyObservers(Event.PLAYERS_CHANGED)

  override def selectPlayer(number: Int): Unit =
    val player = if settings.singlePlayer then game.players.head else game.players(number - 1)
    game = game.setSelectedPlayer(Some(player))
    notifyObservers(Event.PLAYERS_CHANGED)

  override def unselectPlayer(): Unit =
    game = game.setSelectedPlayer(None)
    notifyObservers(Event.PLAYERS_CHANGED)

  override def setMessage(msg: String): Unit =
    game = game.setMessage(msg)
    notifyObservers(Event.MESSAGE_CHANGED)

  override def settingsToString: String = settings.toString

  override def gameToString: String = game.toString

  override def toString: String = if settings.mode == GameMode.SETTINGS then settingsToString else gameToString