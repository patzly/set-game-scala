package de.htwg.se.set.model

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.manager.UndoManager
import de.htwg.se.set.util.InputUtil.{CoordinatesInput, NumberInput, RedoInput, UndoInput}
import de.htwg.se.set.util.{InputUtil, PrintUtil}
import de.htwg.se.set.view.Tui

sealed trait TuiState(tui: Tui):

  def controller: Controller = tui.controller
  
  def undoManager: UndoManager = controller.undoManager

  def run(): Unit

  override def toString: String = PrintUtil.purple(getClass.getSimpleName)
  
case class SettingsState(tui: Tui) extends TuiState(tui):

  override def run(): Unit =
    println(PrintUtil.bold("1") + " Start game")
    println(PrintUtil.bold("2") + " Change number of players")
    println(PrintUtil.bold("3") + " Switch to " + (if controller.settings.easy then "normal" else "easy") + " mode")
    InputUtil.intInput(1, 3, undoManager.canUndo, undoManager.canRedo) match
      case NumberInput(option) =>
        option match
          case 1 =>
            undoManager.doStep(StartGameCommand(controller), this)
            tui.changeState(SelectPlayerState(tui))
          case 2 =>
            tui.changeState(ChangePlayerCountState(tui))
          case 3 =>
            undoManager.doStep(SwitchEasyCommand(controller), this)
      case UndoInput =>
        val state = undoManager.undoStep(this)
        tui.changeState(state)
      case RedoInput =>
        val state = undoManager.redoStep()
        tui.changeState(state)
      case _ =>

case class ChangePlayerCountState(tui: Tui) extends TuiState(tui):

  override def run(): Unit =
    println("Enter number of players:")
    InputUtil.intInput(1, 10, undoManager.canUndo, undoManager.canRedo) match
      case NumberInput(playerCount) =>
        undoManager.doStep(ChangePlayerCountCommand(controller, playerCount), this)
        tui.changeState(SettingsState(tui))
      case UndoInput =>
        val state = undoManager.undoStep(this)
        tui.changeState(state)
      case RedoInput =>
        val state = undoManager.redoStep()
        tui.changeState(state)
      case _ =>

case class SelectPlayerState(tui: Tui) extends TuiState(tui):

  override def run(): Unit =
    if !controller.settings.singlePlayer then
      println("Input player who found a SET (e.g. 1) or 0 if no SET can be found:")
    val input = if controller.settings.singlePlayer then
      NumberInput(1)
    else
      InputUtil.intInput(0, controller.settings.playerCount, undoManager.canUndo, undoManager.canRedo)
    input match
      case NumberInput(input) =>
        if input != 0 then
          undoManager.doStep(SelectPlayerCommand(controller, input), this)
        else
          val command = undoManager.doStep(AddColumnCommand(controller), this)
          if command.asInstanceOf[AddColumnCommand].endGame then
            tui.changeState(GameEndState(tui))
            return;
        tui.changeState(GameState(tui))
      case UndoInput =>
        val state = undoManager.undoStep(this)
        tui.changeState(state)
      case RedoInput =>
        val state = undoManager.redoStep()
        tui.changeState(state)
      case _ =>

case class GameState(tui: Tui) extends TuiState(tui):

  override def run(): Unit =
    val selectedPlayer = controller.game.selectedPlayer
    val player = selectedPlayer match
      case Some(p) => p
      case None =>
        tui.changeState(SelectPlayerState(tui))
        return;
    println("Select 3 cards for a SET (e.g. A1 B2 C3):")
    InputUtil.coordinatesInput(undoManager.canUndo, undoManager.canRedo) match
      case CoordinatesInput(coordinates) =>
        val command = undoManager.doStep(SelectCardsCommand(controller, coordinates), this)
        if command.asInstanceOf[SelectCardsCommand].endGame then tui.changeState(GameEndState(tui))
      case UndoInput =>
        val state = undoManager.undoStep(this)
        tui.changeState(state)
      case RedoInput =>
        undoManager.redoStep()
      case _ =>

case class GameEndState(tui: Tui) extends TuiState(tui):

  override def run(): Unit =
    println("\n" + PrintUtil.yellow(PrintUtil.bold("All SETs found!")))
    if !controller.settings.singlePlayer then
      controller.game.players.sortBy(player => (-player.sets.length, player.number)).foreach(player => println(player))
    println(controller.settingsToString)
    tui.changeState(SettingsState(tui))