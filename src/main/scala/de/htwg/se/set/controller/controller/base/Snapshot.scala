package de.htwg.se.set.controller.controller.base

import de.htwg.se.set.controller.{IController, IState}
import de.htwg.se.set.model.game.base.Game
import de.htwg.se.set.model.settings.base.Settings
import de.htwg.se.set.model.{IGame, ISettings}

import scala.xml.{Elem, Node}

case class Snapshot(settings: ISettings, game: IGame, state: IState):
  
  def toXml: Elem =
    <snapshot>
      {settings.toXml}
      {game.toXml}
      {state.toXml}
    </snapshot>

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