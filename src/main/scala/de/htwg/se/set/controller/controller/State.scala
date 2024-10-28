package de.htwg.se.set.controller.controller

import de.htwg.se.set.controller.{IAction, IController, IState, IUserInput}
import de.htwg.se.set.util.InputUtil.*
import de.htwg.se.set.util.{InputUtil, PrintUtil}
import play.api.libs.json.{JsValue, Json}

import scala.xml.Elem

private class State extends IState:

  def message: String = ""

  def actionFromInput(input: String): IAction = NoAction()

  def handleInput(input: IUserInput): IAction =
    input match
      case UndoInput => UndoAction()
      case RedoInput => RedoAction()
      case InvalidInput(msg) => InvalidAction(msg)
      case _ => NoAction()

  override def toXml: Elem = <state>{getClass.getSimpleName}</state>

  override def toJson: JsValue = Json.toJson(getClass.getSimpleName)

case class SettingsState(controller: IController) extends State:

  override def toString: String =
    PrintUtil.bold("1") + " Start game" + "\n" +
      PrintUtil.bold("2") + " Change number of players" + "\n" +
      PrintUtil.bold("3") + " Switch to " + (if controller.settings.easy then "normal" else "easy") + " mode"

  override def actionFromInput(input: String): IAction =
    InputUtil.intInput(input, 1, 3, controller.canUndo, controller.canRedo, false) match
      case NumberInput(1) => StartGameAction()
      case NumberInput(2) => GoToPlayerCountAction()
      case NumberInput(3) => SwitchEasyAction()
      case other => super.handleInput(other)

case class ChangePlayerCountState(controller: IController) extends State:

  override def toString: String = "Enter number of players:"

  override def actionFromInput(input: String): IAction =
    InputUtil.intInput(input, 1, 10, controller.canUndo, controller.canRedo, false) match
      case NumberInput(number) => ChangePlayerCountAction(number)
      case other => super.handleInput(other)

case class SelectPlayerState(controller: IController) extends State:

  override def toString: String =
    if !controller.settings.singlePlayer then
      "Input player who found a SET (e.g. 1) or 0 if no SET can be found:"
    else
      ""

  override def message: String = "Select player who found a SET or press ADD CARDS if no SET can be found."

  override def actionFromInput(input: String): IAction =
    val userInput = if controller.settings.singlePlayer then
      NumberInput(1)
    else
      InputUtil.intInput(input, 0, controller.settings.playerCount, controller.canUndo, controller.canRedo, true)
    userInput match
      case NumberInput(0) => AddColumnAction()
      case NumberInput(number) => SelectPlayerAction(number)
      case ExitInput => ExitAction()
      case other => super.handleInput(other)

case class GameState(controller: IController) extends State:

  override def toString: String =
    if controller.settings.singlePlayer then
      "Select 3 cards for a SET (e.g. A1 B2 C3):"
    else
      "Player " + player.number + ", select 3 cards for a SET (e.g. A1 B2 C3):"
  
  private def player =
    controller.game.selectedPlayer match 
      case Some(p) => p
      case None => throw IllegalStateException("No player selected")

  override def message: String =
    if controller.settings.singlePlayer then
      "Select 3 cards for a SET."
    else 
      "Player " + player.number + ", select 3 cards for a SET."

  override def actionFromInput(input: String): IAction =
    InputUtil.coordinatesInput(input, controller.canUndo, controller.canRedo) match
      case CoordinatesInput(coordinates) => SelectCardsAction(coordinates)
      case ExitInput => ExitAction()
      case other => super.handleInput(other)

case class GameEndState(controller: IController) extends State:

  override def toString: String =
    val players = controller.game.players
      .sortBy(player => (-player.sets.length, player.number))
      .map(player => player.toString)
      .mkString("\n")
    "\n" + PrintUtil.yellow(PrintUtil.bold("All SETs found!"))
      + (if !controller.settings.singlePlayer then "\n" + players else "")
      + "\nType f to finish:"

  override def actionFromInput(input: String): IAction =
    InputUtil.finishInput(input, controller.canUndo, controller.canRedo) match
      case FinishInput => ExitAction()
      case other => super.handleInput(other)