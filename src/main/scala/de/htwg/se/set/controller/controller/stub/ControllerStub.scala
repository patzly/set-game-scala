package de.htwg.se.set.controller.controller.stub

import de.htwg.se.set.controller.controller.base.*
import de.htwg.se.set.controller.{Event, IAction, IController, IState}
import de.htwg.se.set.model.*
import de.htwg.se.set.model.game.stub.GameStub
import de.htwg.se.set.model.settings.stub.SettingsStub

case class ControllerStub() extends IController:

  var settings: ISettings = SettingsStub()
  var game: IGame = GameStub()

  private val undoManager = new UndoManager
  private var state: IState = SettingsState(this)

  override def changeState(s: IState): Unit =
    state = s
    notifyObservers(Event.STATE_CHANGED)

  override def printState(): Unit = state.print()

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
      case UndoAction() => undoManager.undoCommand()
      case RedoAction() => undoManager.redoCommand()
      case _ =>

  def snapshot: Snapshot = Snapshot(settings, game, state)

  override def restoreSnapshot(snapshot: Snapshot): Unit =
    settings = snapshot.settings
    game = snapshot.game
    state = snapshot.state
    notifyObservers(Event.SETTINGS_OR_GAME_CHANGED)
    notifyObservers(Event.STATE_CHANGED)
    notifyObservers(Event.GAME_MODE_CHANGED)

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