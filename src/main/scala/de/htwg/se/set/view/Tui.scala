package de.htwg.se.set.view

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.{InvalidAction, Action}
import de.htwg.se.set.util.{Event, Observer, PrintUtil}

import scala.annotation.tailrec
import scala.io.StdIn

case class Tui(controller: Controller) extends Observer:

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
  private def actionFromInput: Action =
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