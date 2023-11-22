package de.htwg.se.set.model

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.util.{InputUtil, PrintUtil}
import de.htwg.se.set.view.Tui

sealed trait TuiState(tui: Tui):

  def controller: Controller = tui.controller

  def run(): Unit

case class SettingsState(tui: Tui) extends TuiState(tui):

  override def run(): Unit =
    println(PrintUtil.bold("1") + " Start game")
    println(PrintUtil.bold("2") + " Change number of players")
    println(PrintUtil.bold("3") + " Switch to " + (if controller.settings.easy then "normal" else "easy") + " mode")
    InputUtil.intInput(1, 3) match
      case 1 =>
        StartGameCommand(controller).execute
        tui.changeState(SelectPlayerState(tui))
      case 2 =>
        tui.changeState(ChangePlayerCountState(tui))
      case 3 =>
        SwitchEasyCommand(controller).execute

case class ChangePlayerCountState(tui: Tui) extends TuiState(tui):

  override def run(): Unit =
    println("Enter number of players:")
    val playerCount = InputUtil.intInput(1, 10)
    ChangePlayerCountCommand(controller, playerCount).execute
    tui.changeState(SettingsState(tui))

case class SelectPlayerState(tui: Tui) extends TuiState(tui):

  override def run(): Unit =
    if !controller.settings.singlePlayer then
      println(s"Input player who found a SET (e.g. 1) or 0 if no SET can be found:")
    val input = if controller.settings.singlePlayer then 1 else InputUtil.intInput(0, controller.settings.playerCount)
    if input != 0 then
      SelectPlayerCommand(controller, input).execute
    else
      if AddColumnCommand(controller).execute.endGame then
        tui.changeState(GameEndState(tui))
        return;
    tui.changeState(GameState(tui))

case class GameState(tui: Tui) extends TuiState(tui):

  override def run(): Unit =
    val selectedPlayer = controller.game.selectedPlayer
    val player = selectedPlayer match
      case Some(p) => p
      case None =>
        tui.changeState(SelectPlayerState(tui))
        return;
    println(s"Select 3 cards for a SET (e.g. A1 B2 C3):")
    val coordinates = InputUtil.coordinatesInput
    if SelectCardsCommand(controller, coordinates).execute.endGame then tui.changeState(GameEndState(tui))
    

case class GameEndState(tui: Tui) extends TuiState(tui):

  override def run(): Unit =
    println("\n" + PrintUtil.yellow(PrintUtil.bold("All SETs found!")))
    if !controller.settings.singlePlayer then
      controller.game.players.sortBy(player => (-player.sets.length, player.number)).foreach(player => println(player))
    println(controller.settingsToString)
    tui.changeState(SettingsState(tui))