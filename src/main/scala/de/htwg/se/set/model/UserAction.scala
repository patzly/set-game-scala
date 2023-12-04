package de.htwg.se.set.model

sealed trait UserAction


case object StartGameAction extends UserAction

case object GoToPlayerCountAction extends UserAction

case object SwitchEasyAction extends UserAction

case class ChangePlayerCountAction(playerCount: Int) extends UserAction

case class SelectPlayerAction(number: Int) extends UserAction

case object AddColumnAction extends UserAction

case class SelectCardsAction(coordinates: List[String]) extends UserAction

case object FinishAction extends UserAction

case object UndoAction extends UserAction

case object RedoAction extends UserAction

case class InvalidAction(msg: String) extends UserAction

case object NoAction extends UserAction