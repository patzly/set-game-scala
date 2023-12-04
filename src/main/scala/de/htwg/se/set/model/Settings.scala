package de.htwg.se.set.model

import de.htwg.se.set.util.PrintUtil

case class Settings(playerCount: Int, easy: Boolean, inGame: Boolean = false):

  if playerCount < 1 then
    throw new IllegalArgumentException("Game must have at least one player")

  def singlePlayer: Boolean = playerCount == 1

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