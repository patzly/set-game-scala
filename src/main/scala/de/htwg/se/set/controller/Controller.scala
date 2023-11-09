package de.htwg.se.set.controller

import de.htwg.se.set.model.{Card, Deck, Game, Player, Settings}
import de.htwg.se.set.util.Observable

case class Controller(var settings: Settings, var game: Game) extends Observable:

  def setPlayerCount(count: Int): Unit =
    settings = settings.copy(playerCount = count)
    notifyObservers()
    
  def setEasy(easy: Boolean): Unit =
    settings = settings.copy(easy = easy)
    notifyObservers()
    
  def setColumns(columns: Int): Unit =
    game = game.copy(columns = columns)
    notifyObservers()

  def setDeck(deck: Deck): Unit =
    game = game.copy(deck = deck)
    notifyObservers()

  def setTableCards(cards: List[Card]): Unit =
    game = game.copy(tableCards = cards)
    notifyObservers()

  def setPlayersCards(cards: List[Card]): Unit =
    game = game.copy(playersCards = cards)
    notifyObservers()

  def setPlayers(players: List[Player]): Unit =
    game = game.copy(players = players)
    notifyObservers()

  def settingsToString: String = settings.toString

  def gameToString: String = game.toString