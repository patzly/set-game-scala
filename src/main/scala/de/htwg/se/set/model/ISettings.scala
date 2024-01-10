package de.htwg.se.set.model

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

enum GameMode:
  
  case SETTINGS
  case IN_GAME
  case GAME_END