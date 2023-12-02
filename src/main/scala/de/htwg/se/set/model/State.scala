package de.htwg.se.set.model

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.manager.UndoManager
import de.htwg.se.set.util.InputUtil.{CoordinatesInput, NumberInput, QuitInput, RedoInput, UndoInput}
import de.htwg.se.set.util.{InputUtil, PrintUtil}

sealed trait State(controller: Controller):

  def undoManager: UndoManager = controller.undoManager

  def run(): Unit

case class SettingsState(controller: Controller) extends State(controller):

  override def run(): Unit =
    println(PrintUtil.bold("1") + " Start game")
    println(PrintUtil.bold("2") + " Change number of players")
    println(PrintUtil.bold("3") + " Switch to " + (if controller.settings.easy then "normal" else "easy") + " mode")
    InputUtil.intInput(1, 3, undoManager.canUndo, undoManager.canRedo, false) match
      case NumberInput(option) =>
        option match
          case 1 => undoManager.executeCommand(StartGameCommand(controller))
          case 2 => undoManager.executeCommand(GoToPlayerCountCommand(controller))
          case 3 => undoManager.executeCommand(SwitchEasyCommand(controller))
      case UndoInput => undoManager.undoCommand()
      case RedoInput => undoManager.redoCommand()
      case _ =>

case class ChangePlayerCountState(controller: Controller) extends State(controller):

  override def run(): Unit =
    println("Enter number of players:")
    InputUtil.intInput(1, 10, undoManager.canUndo, undoManager.canRedo, false) match
      case NumberInput(playerCount) => undoManager.executeCommand(ChangePlayerCountCommand(controller, playerCount))
      case UndoInput => undoManager.undoCommand()
      case RedoInput => undoManager.redoCommand()
      case _ =>

case class SelectPlayerState(controller: Controller) extends State(controller):

  override def run(): Unit =
    if !controller.settings.singlePlayer then
      println("Input player who found a SET (e.g. 1) or 0 if no SET can be found:")
    val input = if controller.settings.singlePlayer then
      NumberInput(1)
    else
      InputUtil.intInput(0, controller.settings.playerCount, undoManager.canUndo, undoManager.canRedo, true)
    input match
      case NumberInput(input) =>
        if input != 0 then
          undoManager.executeCommand(SelectPlayerCommand(controller, input))
        else
          undoManager.executeCommand(AddColumnCommand(controller))
      case UndoInput => undoManager.undoCommand()
      case RedoInput => undoManager.redoCommand()
      case QuitInput => undoManager.executeCommand(GoToSettingsCommand(controller))
      case _ =>

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
    InputUtil.coordinatesInput(undoManager.canUndo, undoManager.canRedo) match
      case CoordinatesInput(coordinates) => undoManager.executeCommand(SelectCardsCommand(controller, coordinates))
      case UndoInput => undoManager.undoCommand()
      case RedoInput => undoManager.redoCommand()
      case QuitInput => undoManager.executeCommand(GoToSettingsCommand(controller))
      case _ =>

case class GameEndState(controller: Controller) extends State(controller):

  override def run(): Unit =
    println("\n" + PrintUtil.yellow(PrintUtil.bold("All SETs found!")))
    if !controller.settings.singlePlayer then
      controller.game.players.sortBy(player => (-player.sets.length, player.number)).foreach(player => println(player))
    undoManager.executeCommand(GoToSettingsCommand(controller))