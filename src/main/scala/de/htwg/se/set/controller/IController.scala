package de.htwg.se.set.controller

import de.htwg.se.set.controller.controller.Snapshot
import de.htwg.se.set.model.*
import de.htwg.se.set.util.Observable
import play.api.libs.json.JsValue

import scala.xml.Elem

trait IController extends Observable:
  
  def settings: ISettings
  def game: IGame
  
  def changeState(s: IState): Unit
  def currentState: String
  def actionFromInput(input: String): IAction
  def handleAction(action: IAction): Unit
  def snapshot: Snapshot
  def restoreSnapshot(snapshot: Snapshot): Unit
  def save(): Unit
  def canUndo: Boolean
  def canRedo: Boolean
  def setPlayerCount(count: Int): Unit
  def setEasy(easy: Boolean): Unit
  def setGameMode(mode: GameMode): Unit
  def setColumns(columns: Int): Unit
  def addColumn(): Unit
  def removeColumn(): Unit
  def setDeck(deck: IDeck): Unit
  def setTableCards(cards: List[ICard]): Unit
  def setPlayersCards(cards: List[ICard]): Unit
  def setPlayers(players: List[IPlayer]): Unit
  def updatePlayer(player: IPlayer): Unit
  def selectPlayer(number: Int): Unit
  def unselectPlayer(): Unit
  def setMessage(msg: String): Unit
  def settingsToString: String
  def gameToString: String

trait IAction

trait ICommand(controller: IController):

  def saveSnapshot(): Unit
  def undo(): Unit
  def execute(): Unit

trait ISnapshot:

  def toXml: Elem
  def toJson: JsValue

trait IState:
  
  def message: String
  def actionFromInput(input: String): IAction
  def handleInput(input: IUserInput): IAction
  def toXml: Elem
  def toJson: JsValue

trait IUserInput

enum Event:
  case SETTINGS_CHANGED
  case CARDS_CHANGED
  case PLAYERS_CHANGED
  case COLUMNS_CHANGED
  case SETTINGS_OR_GAME_CHANGED
  case STATE_CHANGED
  case MESSAGE_CHANGED
  case GAME_MODE_CHANGED