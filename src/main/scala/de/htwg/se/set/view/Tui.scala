package de.htwg.se.set.view

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.util.{Event, Observer}

import scala.annotation.tailrec
import scala.io.StdIn

case class Tui(controller: Controller) extends Observer:

  controller.add(this)

  def run(): Unit =
    println(controller.settingsToString)
    loop()

  @tailrec
  private def loop(): Unit =
    controller.runState()
    controller.handleAction(controller.actionFromInput(StdIn.readLine))
    loop()

  override def update(event: Event): Unit =
    event match
      case Event.SETTINGS_CHANGED => println(controller.settingsToString)
      case Event.CARDS_CHANGED => println(controller.gameToString)
      case Event.SETTINGS_OR_GAME_CHANGED => println(controller)
      case _ =>