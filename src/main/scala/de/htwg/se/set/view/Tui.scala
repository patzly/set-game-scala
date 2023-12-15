package de.htwg.se.set.view

import de.htwg.se.set.controller.{Event, IAction, IController}
import de.htwg.se.set.controller.controller.baseImpl.InvalidAction
import de.htwg.se.set.util.{Observer, PrintUtil}

import scala.annotation.tailrec
import scala.io.StdIn

case class Tui(controller: IController) extends Observer:

  controller.add(this)

  println(PrintUtil.bold("Welcome to the SET Game!"))
  println(controller.settingsToString)
  controller.printState()
  loop()

  @tailrec
  private def loop(): Unit =
    controller.handleAction(actionFromInput)
    loop()

  @tailrec
  private def actionFromInput: IAction =
    controller.actionFromInput(StdIn.readLine) match
      case InvalidAction(msg) =>
        println(PrintUtil.red(msg + " Try again:"))
        actionFromInput
      case action => action

  override def update(event: Event): Unit =
    event match
      case Event.SETTINGS_CHANGED => println(controller.settingsToString)
      case Event.CARDS_CHANGED => println(controller.gameToString)
      case Event.SETTINGS_OR_GAME_CHANGED => println(controller)
      case Event.STATE_CHANGED => controller.printState()
      case _ =>