package de.htwg.se.set.controller.controller.baseImpl

import de.htwg.se.set.controller.IAction

case object StartGameAction extends IAction

case object GoToPlayerCountAction extends IAction

case object SwitchEasyAction extends IAction

case class ChangePlayerCountAction(playerCount: Int) extends IAction

case class SelectPlayerAction(number: Int) extends IAction

case object AddColumnAction extends IAction

case class SelectCardsAction(coordinates: List[String]) extends IAction

case object ExitAction extends IAction

case object UndoAction extends IAction

case object RedoAction extends IAction

case class InvalidAction(msg: String) extends IAction

case object NoAction extends IAction