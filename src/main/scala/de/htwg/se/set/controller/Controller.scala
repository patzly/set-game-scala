package de.htwg.se.set.controller

import de.htwg.se.set.model.{Card, Deck, Game, Player, Settings}
import de.htwg.se.set.util.{Event, Observable}

case class Controller(var settings: Settings, var game: Game) extends Observable:

  def setPlayerCount(count: Int): Unit =
    settings = settings.copy(playerCount = count)
    notifyObservers(Event.SETTINGS_CHANGED)
    
  def setEasy(easy: Boolean): Unit =
    settings = settings.copy(easy = easy)
    notifyObservers(Event.SETTINGS_CHANGED)

  def setInGame(inGame: Boolean): Unit = settings = settings.copy(inGame = inGame)
    
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

  override def toString: String = if settings.inGame then game.toString else settings.toString