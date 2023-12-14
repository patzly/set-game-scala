package de.htwg.se.set.model

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.util.InputUtil.*
import de.htwg.se.set.util.{InputUtil, PrintUtil}

sealed trait State(controller: Controller):

  def print(): Unit

  def message: String = ""

  def actionFromInput(input: String): Action

  def handleInput(input: UserInput): Action =
    input match
      case UndoInput => UndoAction
      case RedoInput => RedoAction
      case InvalidInput(msg) => InvalidAction(msg)
      case _ => NoAction

case class SettingsState(controller: Controller) extends State(controller):

  override def print(): Unit =
    println(PrintUtil.bold("1") + " Start game")
    println(PrintUtil.bold("2") + " Change number of players")
    println(PrintUtil.bold("3") + " Switch to " + (if controller.settings.easy then "normal" else "easy") + " mode")

  override def actionFromInput(input: String): Action =
    InputUtil.intInput(input, 1, 3, controller.canUndo, controller.canRedo) match
      case NumberInput(1) => StartGameAction
      case NumberInput(2) => GoToPlayerCountAction
      case NumberInput(3) => SwitchEasyAction
      case other => super.handleInput(other)

case class ChangePlayerCountState(controller: Controller) extends State(controller):

  override def print(): Unit = println("Enter number of players:")

  override def actionFromInput(input: String): Action =
    InputUtil.intInput(input, 1, 10, controller.canUndo, controller.canRedo) match
      case NumberInput(number) => ChangePlayerCountAction(number)
      case other => super.handleInput(other)

case class SelectPlayerState(controller: Controller) extends State(controller):

  override def print(): Unit =
    if !controller.settings.singlePlayer then
      println("Input player who found a SET (e.g. 1) or 0 if no SET can be found:")

  override def message: String = "Select player who found a SET or press ADD CARDS if no SET can be found."

  override def actionFromInput(input: String): Action =
    val userInput = if controller.settings.singlePlayer then
      NumberInput(1)
    else
      InputUtil.intInput(input, 0, controller.settings.playerCount, controller.canUndo, controller.canRedo)
    userInput match
      case NumberInput(0) => AddColumnAction
      case NumberInput(number) => SelectPlayerAction(number)
      case other => super.handleInput(other)

case class GameState(controller: Controller) extends State(controller):
  
  private def player =
    controller.game.selectedPlayer match 
      case Some(p) => p
      case None => throw IllegalStateException("No player selected")

  override def print(): Unit =
    if controller.settings.singlePlayer then
      println("Select 3 cards for a SET (e.g. A1 B2 C3):")
    else
      println("Player " + player.number + ", select 3 cards for a SET (e.g. A1 B2 C3):")

  override def message: String =
    if controller.settings.singlePlayer then
      "Select 3 cards for a SET."
    else 
      "Player " + player.number + ", select 3 cards for a SET."

  override def actionFromInput(input: String): Action =
    InputUtil.coordinatesInput(input, controller.canUndo, controller.canRedo) match
      case CoordinatesInput(coordinates) => SelectCardsAction(coordinates)
      case other => super.handleInput(other)

case class GameEndState(controller: Controller) extends State(controller):

  override def print(): Unit =
    println("\n" + PrintUtil.yellow(PrintUtil.bold("All SETs found!")))
    if !controller.settings.singlePlayer then
      controller.game.players.sortBy(player => (-player.sets.length, player.number)).foreach(player => println(player))
    println("\nType f to finish:")

  override def actionFromInput(input: String): Action =
    InputUtil.finishInput(input, controller.canUndo, controller.canRedo) match
      case FinishInput => FinishAction
      case other => super.handleInput(other)