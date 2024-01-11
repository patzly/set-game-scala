package de.htwg.se.set.model.settings.base

import com.google.inject.Inject
import com.google.inject.name.Named
import de.htwg.se.set.model.{GameMode, ISettings}
import de.htwg.se.set.util.PrintUtil
import play.api.libs.json.{JsValue, Json}

import scala.xml.{Elem, Node}

case class Settings @Inject() (@Named("playerCount") playerCount: Int,
                               @Named("easy") easy: Boolean,
                               mode: GameMode = GameMode.SETTINGS) extends ISettings:

  if playerCount < 1 then throw IllegalArgumentException("Game must have at least one player")

  override def singlePlayer: Boolean = playerCount == 1

  override def setPlayerCount(playerCount: Int): ISettings = copy(playerCount = playerCount)

  override def setEasy(easy: Boolean): ISettings = copy(easy = easy)

  override def setGameMode(mode: GameMode): ISettings = copy(mode = mode)
  
  override def toXml: Elem =
    <settings>
      <playerCount>{playerCount}</playerCount>
      <easy>{easy}</easy>
      <mode>{mode}</mode>
    </settings>

  override def toJson: JsValue = Json.obj(
    "playerCount" -> Json.toJson(playerCount),
    "easy" -> Json.toJson(easy),
    "mode" -> Json.toJson(mode.toString)
  )

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

object Settings:
  
  def fromXml(node: Node): Settings =
    val playerCount = (node \ "playerCount").text.toInt
    val easy = (node \ "easy").text.toBoolean
    val mode = GameMode.valueOf((node \ "mode").text)
    Settings(playerCount, easy, mode)

  def fromJson(json: JsValue): Settings =
    val playerCount = (json \ "playerCount").get.as[Int]
    val easy = (json \ "easy").get.as[Boolean]
    val mode = GameMode.valueOf((json \ "mode").get.as[String])
    Settings(playerCount, easy, mode)