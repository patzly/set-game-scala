package de.htwg.se.set.model.game.base

import com.google.inject.Inject
import com.google.inject.name.Named
import de.htwg.se.set.model.{ICard, IDeck, IGame, IPlayer}
import play.api.libs.json.{JsValue, Json, Reads, Writes}

import scala.xml.{Elem, Node, NodeSeq, Null}

case class Game @Inject() (@Named("columns") columns: Int,
                           deck: IDeck, tableCards: List[ICard], playersCards: List[ICard],
                           players: List[IPlayer], selectedPlayer: Option[IPlayer], message: String = "") extends IGame:

  override def setColumns(columns: Int): IGame = copy(columns = columns)

  override def setDeck(deck: IDeck): IGame = copy(deck = deck)

  override def setTableCards(tableCards: List[ICard]): IGame = copy(tableCards = tableCards)

  override def setPlayersCards(playersCards: List[ICard]): IGame = copy(playersCards = playersCards)

  override def setPlayers(players: List[IPlayer]): IGame = copy(players = players)

  override def setSelectedPlayer(selectedPlayer: Option[IPlayer]): IGame = copy(selectedPlayer = selectedPlayer)

  override def setMessage(message: String): IGame = copy(message = message)

  override def toXml: Elem =
    <game>
      <columns>{columns}</columns>
      {deck.toXml}
      <tableCards>{tableCards.map(_.toXml)}</tableCards>
      <playersCards>{playersCards.map(_.toXml)}</playersCards>
      <players>{players.map(_.toXml)}</players>
      <selectedPlayer>{selectedPlayer.map(player => player.toXml).getOrElse(Null)}</selectedPlayer>
      <message>{message}</message>
    </game>

  override def toJson: JsValue = Json.obj(
    "columns" -> Json.toJson(columns),
    "deck" -> deck.toJson,
    "tableCards" -> Json.toJson(tableCards)(Writes.seq[ICard](Card.writes)),
    "playersCards" -> Json.toJson(playersCards)(Writes.seq[ICard](Card.writes)),
    "players" -> Json.toJson(players)(Writes.seq[IPlayer](Player.writes)),
    "selectedPlayer" -> selectedPlayer.map(_.toJson),
    "message" -> Json.toJson(message)
  )

  override def toString: String = "\n" + Grid(columns, tableCards, deck.easy)

object Game:
  
  def fromXml(node: Node): IGame =
    val columns = (node \ "columns").text.toInt
    val deck = Deck.fromXml((node \ "deck").head)
    val tableCards = (node \ "tableCards" \ "card").map(Card.fromXml).toList
    val playersCards = (node \ "playersCards" \ "card").map(Card.fromXml).toList
    val players = (node \ "players" \ "player").map(player => Player.fromXml(player)).toList
    val selectedPlayer = (node \ "selectedPlayer").headOption
      .flatMap(n => (n \ "player").headOption.map(Player.fromXml))
    val message = (node \ "message").text
    Game(columns, deck, tableCards, playersCards, players, selectedPlayer, message)
    
  def fromJson(json: JsValue): IGame =
    val columns = (json \ "columns").as[Int]
    val deck = Deck.fromJson((json \ "deck").get)
    val tableCards = (json \ "tableCards").as[List[JsValue]].map(Card.fromJson)
    val playersCards = (json \ "playersCards").as[List[JsValue]].map(Card.fromJson)
    val players = (json \ "players").as[List[JsValue]].map(Player.fromJson)
    val selectedPlayer = (json \ "selectedPlayer").asOpt[IPlayer](Player.reads)
    val message = (json \ "message").as[String]
    Game(columns, deck, tableCards, playersCards, players, selectedPlayer, message)