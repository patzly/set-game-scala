package de.htwg.se.set.model.game

import de.htwg.se.set.model.{ICard, IDeck, IGame, IPlayer}

case class Game(columns: Int, deck: IDeck, tableCards: List[ICard], playersCards: List[ICard], players: List[IPlayer],
                selectedPlayer: Option[IPlayer], message: String = "") extends IGame:

  def setColumns(columns: Int): IGame = copy(columns = columns)

  def setDeck(deck: IDeck): IGame = copy(deck = deck)

  def setTableCards(tableCards: List[ICard]): IGame = copy(tableCards = tableCards)

  def setPlayersCards(playersCards: List[ICard]): IGame = copy(playersCards = playersCards)

  def setPlayers(players: List[IPlayer]): IGame = copy(players = players)

  def setSelectedPlayer(selectedPlayer: Option[IPlayer]): IGame = copy(selectedPlayer = selectedPlayer)

  def setMessage(message: String): IGame = copy(message = message)

  override def toString: String = "\n" + Grid(columns, tableCards, deck.easy)