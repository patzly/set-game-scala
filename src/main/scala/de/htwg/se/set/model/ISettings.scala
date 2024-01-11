package de.htwg.se.set.model

import play.api.libs.json.{JsError, JsResult, JsSuccess, JsValue, Json, Reads, Writes}

import scala.xml.Elem

trait ISettings:
  
  def playerCount: Int
  def easy: Boolean
  def mode: GameMode

  def singlePlayer: Boolean
  def setPlayerCount(playerCount: Int): ISettings
  def setEasy(easy: Boolean): ISettings
  def setGameMode(mode: GameMode): ISettings
  def toXml: Elem
  def toJson: JsValue

enum GameMode:
  
  case SETTINGS
  case IN_GAME
  case GAME_END