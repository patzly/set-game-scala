package de.htwg.se.set.controller

import de.htwg.se.set.manager.{Snapshot, UndoManager}
import de.htwg.se.set.model.{Card, Deck, Game, Player, Settings, SettingsState, State}
import de.htwg.se.set.util.{Event, Observable}

case class Controller(var settings: Settings, var game: Game) extends Observable:

  val undoManager: UndoManager = UndoManager()
  private var state: State = SettingsState(this)

  def changeState(s: State): Unit = state = s
  
  def runState(): Unit = state.run()

  def snapshot: Snapshot = Snapshot(settings, game, state)

  def restoreSnapshot(snapshot: Snapshot): State =
    val settingsChanged = settings != snapshot.settings
    val gameChanged = game != snapshot.game
    settings = snapshot.settings
    game = snapshot.game
    if settingsChanged then notifyObservers(Event.SETTINGS_CHANGED)
    if gameChanged then notifyObservers(Event.CARDS_CHANGED)
    snapshot.state

  def setPlayerCount(count: Int): Unit =
    settings = settings.copy(playerCount = count)
    notifyObservers(Event.SETTINGS_CHANGED)
    
  def setEasy(easy: Boolean): Unit =
    settings = settings.copy(easy = easy)
    notifyObservers(Event.SETTINGS_CHANGED)
    
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

  def settingsToString: String = settings.toString

  def gameToString: String = game.toString