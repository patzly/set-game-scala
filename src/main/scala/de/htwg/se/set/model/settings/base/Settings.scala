package de.htwg.se.set.model.settings.base

import com.google.inject.Inject
import com.google.inject.name.Named
import de.htwg.se.set.model.{GameMode, ISettings}
import de.htwg.se.set.util.PrintUtil

case class Settings @Inject() (@Named("playerCount") playerCount: Int,
                               @Named("easy") easy: Boolean,
                               mode: GameMode = GameMode.SETTINGS) extends ISettings:

  if playerCount < 1 then throw IllegalArgumentException("Game must have at least one player")

  override def singlePlayer: Boolean = playerCount == 1

  override def setPlayerCount(playerCount: Int): ISettings = copy(playerCount = playerCount)

  override def setEasy(easy: Boolean): ISettings = copy(easy = easy)

  override def setGameMode(mode: GameMode): ISettings = copy(mode = mode)

  override def equals(obj: Any): Boolean = obj match
    case other: Settings => playerCount == other.playerCount && easy == other.easy
    case _ => false

  override def hashCode: Int =
    val prime = 31
    var result = 1
    result = prime * result + playerCount
    result = prime * result + easy.hashCode
    result

  override def toString: String =
    val playersString = if playerCount == 1 then "1 player" else s"$playerCount players"
    val easyString = if easy then "easy mode" else "normal mode"
    PrintUtil.blue(PrintUtil.bold("\nSettings: ") + PrintUtil.yellow(playersString + ", " + easyString))