package de.htwg.se.set.model.game.stub

import de.htwg.se.set.model.game.base.Deck
import de.htwg.se.set.model.{ICard, IDeck, IGame, IPlayer}

case class GameStub() extends IGame:

  def columns: Int = 4

  def deck: IDeck = Deck(false)

  def tableCards: List[ICard] = ???

  def playersCards: List[ICard] = ???

  def players: List[IPlayer] = ???

  def selectedPlayer: Option[IPlayer] = ???

  def message: String = ???

  def setColumns(columns: Int): IGame = ???

  def setDeck(deck: IDeck): IGame = ???

  def setTableCards(tableCards: List[ICard]): IGame = ???

  def setPlayersCards(playersCards: List[ICard]): IGame = ???

  def setPlayers(players: List[IPlayer]): IGame = ???

  def setSelectedPlayer(selectedPlayer: Option[IPlayer]): IGame = ???

  def setMessage(message: String): IGame = ???