package de.htwg.se.set.model.game.stub

import de.htwg.se.set.model.game.base.Deck
import de.htwg.se.set.model.{ICard, IDeck, IGame, IPlayer}
import play.api.libs.json.JsValue

import scala.xml.Elem

case class GameStub() extends IGame:

  override def columns: Int = 4

  override def deck: IDeck = Deck(false)

  override def tableCards: List[ICard] = ???

  override def playersCards: List[ICard] = ???

  override def players: List[IPlayer] = ???

  override def selectedPlayer: Option[IPlayer] = ???

  override def message: String = ???

  override def setColumns(columns: Int): IGame = ???

  override def setDeck(deck: IDeck): IGame = ???

  override def setTableCards(tableCards: List[ICard]): IGame = ???

  override def setPlayersCards(playersCards: List[ICard]): IGame = ???

  override def setPlayers(players: List[IPlayer]): IGame = ???

  override def setSelectedPlayer(selectedPlayer: Option[IPlayer]): IGame = ???

  override def setMessage(message: String): IGame = ???

  override def toXml: Elem = ???

  override def toJson: JsValue = ???