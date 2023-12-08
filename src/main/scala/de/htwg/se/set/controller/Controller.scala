package de.htwg.se.set.controller

import de.htwg.se.set.manager.{Snapshot, UndoManager}
import de.htwg.se.set.model.*
import de.htwg.se.set.util.{Event, Observable}

case class Controller(var settings: Settings, var game: Game) extends Observable:

  private val undoManager: UndoManager = UndoManager()
  private var state: State = SettingsState(this)

  def changeState(s: State): Unit =
    state = s
    notifyObservers(Event.STATE_CHANGED)
  
  def runState(): Unit = state.run()

  def actionFromInput(input: String): Action = state.actionFromInput(input)

  def handleAction(action: Action): Unit =
    action match
      case StartGameAction => undoManager.executeCommand(StartGameCommand(this))
      case GoToPlayerCountAction => undoManager.executeCommand(GoToPlayerCountCommand(this))
      case SwitchEasyAction => undoManager.executeCommand(SwitchEasyCommand(this))
      case ChangePlayerCountAction(playerCount) => undoManager.executeCommand(ChangePlayerCountCommand(this, playerCount))
      case SelectPlayerAction(number) => undoManager.executeCommand(SelectPlayerCommand(this, number))
      case AddColumnAction => undoManager.executeCommand(AddColumnCommand(this))
      case SelectCardsAction(coordinates) => undoManager.executeCommand(SelectCardsCommand(this, coordinates))
      case FinishAction => undoManager.executeCommand(FinishCommand(this))
      case UndoAction => undoManager.undoCommand()
      case RedoAction => undoManager.redoCommand()
      case _ =>

  def snapshot: Snapshot = Snapshot(settings, game, state)

  def restoreSnapshot(snapshot: Snapshot): Unit =
    settings = snapshot.settings
    game = snapshot.game
    state = snapshot.state
    notifyObservers(Event.SETTINGS_OR_GAME_CHANGED)
    notifyObservers(Event.STATE_CHANGED)
    notifyObservers(Event.GAME_MODE_CHANGED)
    
  def canUndo: Boolean = undoManager.canUndo

  def canRedo: Boolean = undoManager.canRedo

  def setPlayerCount(count: Int): Unit =
    settings = settings.copy(playerCount = count)
    notifyObservers(Event.SETTINGS_CHANGED)
    
  def setEasy(easy: Boolean): Unit =
    settings = settings.copy(easy = easy)
    notifyObservers(Event.SETTINGS_CHANGED)

  def setGameMode(mode: GameMode): Unit =
    settings = settings.copy(mode = mode)
    notifyObservers(Event.GAME_MODE_CHANGED)
    
  def setColumns(columns: Int): Unit =
    game = game.copy(columns = columns)
    notifyObservers(Event.COLUMNS_CHANGED)

  def addColumn(): Unit =
    game = game.copy(columns = game.columns + 1)
    notifyObservers(Event.COLUMNS_CHANGED)

  def removeColumn(): Unit =
    game = game.copy(columns = game.columns - 1 max 1)
    notifyObservers(Event.COLUMNS_CHANGED)

  def setDeck(deck: Deck): Unit = game = game.copy(deck = deck)

  def setTableCards(cards: List[Card]): Unit =
    game = game.copy(tableCards = cards)
    notifyObservers(Event.CARDS_CHANGED)

  def setPlayersCards(cards: List[Card]): Unit = game = game.copy(playersCards = cards)

  def setPlayers(players: List[Player]): Unit =
    game = game.copy(players = players)
    notifyObservers(Event.PLAYERS_CHANGED)

  def updateAndUnselectPlayer(player: Player): Unit =
    game = game.copy(players = game.players.updated(player.index, player), selectedPlayer = None)
    notifyObservers(Event.PLAYERS_CHANGED)

  def selectPlayer(number: Int): Unit =
    val player = if settings.singlePlayer then game.players.head else game.players(number - 1)
    game = game.copy(selectedPlayer = Some(player))
    notifyObservers(Event.PLAYERS_CHANGED)

  def settingsToString: String = settings.toString

  def gameToString: String = game.toString

  override def toString: String = if settings.mode == GameMode.SETTINGS then settingsToString else gameToString