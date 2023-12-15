package de.htwg.se.set.model

trait IGame:
  
  def columns: Int
  def deck: IDeck
  def tableCards: List[ICard]
  def playersCards: List[ICard]
  def players: List[IPlayer]
  def selectedPlayer: Option[IPlayer]
  def message: String
  
  def setColumns(columns: Int): IGame
  def setDeck(deck: IDeck): IGame
  def setTableCards(tableCards: List[ICard]): IGame
  def setPlayersCards(playersCards: List[ICard]): IGame
  def setPlayers(players: List[IPlayer]): IGame
  def setSelectedPlayer(selectedPlayer: Option[IPlayer]): IGame
  def setMessage(message: String): IGame

trait ICard:
  
  def number: Int
  def color: Color
  def symbol: Symbol
  def shading: Shading
  def selected: Boolean
  
  def select: ICard
  def unselect: ICard

trait IDeck:

  def easy: Boolean

  def findSets(cards: List[ICard]): List[ITriplet]
  def tableCards(columns: Int, table: List[ICard], players: List[ICard]): List[ICard]
  def tableCardsSinglePlayer(columns: Int): List[ICard]
  def stapleCards(table: List[ICard], players: List[ICard]): List[ICard]
  def playersCardsAdd(playersCards: List[ICard], set: ITriplet): List[ICard]
  def selectCards(table: List[ICard], card1: ICard, card2: ICard, card3: ICard): List[ICard]
  def unselectCards(table: List[ICard]): List[ICard]
  def cardAtCoordinate(tableCards: List[ICard], coordinate: String, columns: Int): ICard

trait IGrid:

  def columns: Int
  def cards: List[ICard]
  def easy: Boolean

trait IPlayer:
  
  def number: Int
  def singlePlayer: Boolean
  def easy: Boolean
  def sets: List[ITriplet]

  def setSets(sets: List[ITriplet]): IPlayer
  
  def index: Int

trait ITriplet:

  def card1: ICard
  def card2: ICard
  def card3: ICard

  def isSet: Boolean

enum Color:
  case RED, GREEN, BLUE

  override def toString: String =
    this match
      case RED => "R"
      case GREEN => "G"
      case BLUE => "B"

enum Symbol:
  case OVAL, SQUIGGLE, DIAMOND

  override def toString: String =
    this match
      case OVAL => "O"
      case SQUIGGLE => "S"
      case DIAMOND => "D"

enum Shading:
  case SOLID, OUTLINED, STRIPED

  override def toString: String =
    this match
      case SOLID => "F" // filled
      case OUTLINED => "L" // line
      case STRIPED => "C" // checkered