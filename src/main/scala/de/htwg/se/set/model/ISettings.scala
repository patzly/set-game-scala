package de.htwg.se.set.model

trait ISettings:
  
  def playerCount: Int
  def easy: Boolean
  def mode: GameMode

  def setPlayerCount(playerCount: Int): ISettings
  def setEasy(easy: Boolean): ISettings
  def setGameMode(mode: GameMode): ISettings
  
  def singlePlayer: Boolean

enum GameMode:
  case SETTINGS
  case IN_GAME
  case GAME_END