package de.htwg.se.set.model

sealed trait Action

case object StartGameAction extends Action

case object GoToPlayerCountAction extends Action

case object SwitchEasyAction extends Action

case class ChangePlayerCountAction(playerCount: Int) extends Action

case class SelectPlayerAction(number: Int) extends Action

case object AddColumnAction extends Action

case class SelectCardsAction(coordinates: List[String]) extends Action

case object ExitAction extends Action

case object UndoAction extends Action

case object RedoAction extends Action

case class InvalidAction(msg: String) extends Action

case object NoAction extends Action