package de.htwg.se.set.controller.controller

import de.htwg.se.set.controller.IAction
import play.api.libs.json.JsValue

import scala.xml.Node

case class StartGameAction() extends IAction

case class GoToPlayerCountAction() extends IAction

case class SwitchEasyAction() extends IAction

case class ChangePlayerCountAction(playerCount: Int) extends IAction

case class SelectPlayerAction(number: Int) extends IAction

case class AddColumnAction() extends IAction

case class SelectCardsAction(coordinates: List[String]) extends IAction

case class ExitAction() extends IAction

case class LoadXmlAction(node: Node) extends IAction

case class LoadJsonAction(json: JsValue) extends IAction

case class UndoAction() extends IAction

case class RedoAction() extends IAction

case class InvalidAction(msg: String) extends IAction

case class NoAction() extends IAction