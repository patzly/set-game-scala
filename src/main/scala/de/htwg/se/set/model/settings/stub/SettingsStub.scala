package de.htwg.se.set.model.settings.stub

import de.htwg.se.set.model.GameMode.IN_GAME
import de.htwg.se.set.model.{GameMode, ISettings}

case class SettingsStub() extends ISettings:

  override def playerCount: Int = 2

  override def easy: Boolean = false

  override def mode: GameMode = IN_GAME

  override def setPlayerCount(playerCount: Int): ISettings = ???

  override def setEasy(easy: Boolean): ISettings = ???

  override def setGameMode(mode: GameMode): ISettings = ???

  override def singlePlayer: Boolean = playerCount == 1