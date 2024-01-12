package de.htwg.se.set.controller.controller.base

import de.htwg.se.set.controller.{IController, ISnapshot, IState}
import de.htwg.se.set.model.game.base.Game
import de.htwg.se.set.model.settings.base.Settings
import de.htwg.se.set.model.{IGame, ISettings}
import play.api.libs.json.{JsValue, Json}

import java.security.MessageDigest
import scala.xml.{Elem, Node}

case class Snapshot(settings: ISettings, game: IGame, state: IState) extends ISnapshot:
  
  override def toXml: Elem =
    <snapshot>
      {settings.toXml}
      {game.toXml}
      {state.toXml}
    </snapshot>

  override def toJson: JsValue = Json.obj(
    "settings" -> settings.toJson,
    "game" -> game.toJson,
    "state" -> state.toJson
  )

object Snapshot:
  
  def fromXml(node: Node, controller: IController): Snapshot =
    val settings = Settings.fromXml((node \ "settings").head)
    val game = Game.fromXml((node \ "game").head)
    val state = (node \ "state").text match
      case "SettingsState" => SettingsState(controller)
      case "ChangePlayerCountState" => ChangePlayerCountState(controller)
      case "SelectPlayerState" => SelectPlayerState(controller)
      case "GameState" => GameState(controller)
      case "GameEndState" => GameEndState(controller)
      case _ => throw IllegalArgumentException("Invalid state")
    Snapshot(settings, game, state)

  def fromJson(json: JsValue, controller: IController): Snapshot =
    val settings = Settings.fromJson((json \ "settings").get)
    val game = Game.fromJson((json \ "game").get)
    val state = (json \ "state").as[String] match
      case "SettingsState" => SettingsState(controller)
      case "ChangePlayerCountState" => ChangePlayerCountState(controller)
      case "SelectPlayerState" => SelectPlayerState(controller)
      case "GameState" => GameState(controller)
      case "GameEndState" => GameEndState(controller)
      case _ => throw IllegalArgumentException("Invalid state")
    Snapshot(settings, game, state)

  def hash(input: String): String =
    val md = MessageDigest.getInstance("SHA-256")
    val hashBytes = md.digest(input.getBytes("UTF-8"))
    hashBytes.map("%02x".format(_)).mkString