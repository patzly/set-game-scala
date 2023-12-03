package de.htwg.se.set.model

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.util.InputUtil.{CoordinatesInput, FinishInput, NumberInput, RedoInput, UndoInput}
import de.htwg.se.set.util.{InputUtil, PrintUtil}

sealed trait State(controller: Controller):

  def run(): Unit

  def actionFromInput: UserAction

case class SettingsState(controller: Controller) extends State(controller):

  override def run(): Unit =
    println(PrintUtil.bold("1") + " Start game")
    println(PrintUtil.bold("2") + " Change number of players")
    println(PrintUtil.bold("3") + " Switch to " + (if controller.settings.easy then "normal" else "easy") + " mode")

  override def actionFromInput: UserAction =
    InputUtil.intInput(1, 3, controller.canUndo, controller.canRedo) match
      case NumberInput(1) => StartGameAction
      case NumberInput(2) => GoToPlayerCountAction
      case NumberInput(3) => SwitchEasyAction
      case UndoInput => UndoAction
      case RedoInput => RedoAction
      case _ => NoAction

case class ChangePlayerCountState(controller: Controller) extends State(controller):

  override def run(): Unit = println("Enter number of players:")

  override def actionFromInput: UserAction =
    InputUtil.intInput(1, 10, controller.canUndo, controller.canRedo) match
      case NumberInput(input) => ChangePlayerCountAction(input)
      case UndoInput => UndoAction
      case RedoInput => RedoAction
      case _ => NoAction

case class SelectPlayerState(controller: Controller) extends State(controller):

  override def run(): Unit =
    if !controller.settings.singlePlayer then
      println("Input player who found a SET (e.g. 1) or 0 if no SET can be found:")

  override def actionFromInput: UserAction =
    val userInput = if controller.settings.singlePlayer then
      NumberInput(1)
    else
      InputUtil.intInput(0, controller.settings.playerCount, controller.canUndo, controller.canRedo)
    userInput match
      case NumberInput(0) => AddColumnAction
      case NumberInput(input) => SelectPlayerAction(input)
      case UndoInput => UndoAction
      case RedoInput => RedoAction
      case _ => NoAction

case class GameState(controller: Controller) extends State(controller):

  override def run(): Unit =
    val selectedPlayer = controller.game.selectedPlayer
    val player = selectedPlayer match
      case Some(p) => p
      case None => throw IllegalStateException("No player selected")
    if controller.settings.singlePlayer then
      println("Select 3 cards for a SET (e.g. A1 B2 C3):")
    else
      println("Player " + player.number + ", select 3 cards for a SET (e.g. A1 B2 C3):")

  override def actionFromInput: UserAction =
    InputUtil.coordinatesInput(controller.canUndo, controller.canRedo) match
      case CoordinatesInput(coordinates) => SelectCardsAction(coordinates)
      case UndoInput => UndoAction
      case RedoInput => RedoAction
      case _ => NoAction

case class GameEndState(controller: Controller) extends State(controller):

  override def run(): Unit =
    println("\n" + PrintUtil.yellow(PrintUtil.bold("All SETs found!")))
    if !controller.settings.singlePlayer then
      controller.game.players.sortBy(player => (-player.sets.length, player.number)).foreach(player => println(player))
    println("Type f to finish and return to settings:")

  override def actionFromInput: UserAction =
    InputUtil.finishInput(controller.canUndo, controller.canRedo) match
      case FinishInput => FinishAction
      case UndoInput => UndoAction
      case RedoInput => RedoAction
      case _ => NoAction