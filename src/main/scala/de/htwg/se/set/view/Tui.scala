package de.htwg.se.set.view

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.{SettingsState, TuiState}
import de.htwg.se.set.util.{Event, Observer}

import scala.annotation.tailrec

case class Tui(controller: Controller) extends Observer:

  controller.add(this)

  private var state: TuiState = SettingsState(this)

  def changeState(newState: TuiState): Unit = state = newState

  def run(): Unit =
    println(controller.settingsToString)
    stateLoop()

  @tailrec
  private def stateLoop(): Unit =
    state.run()
    stateLoop()

  override def update(event: Event): Unit =
    event match
      case Event.SETTINGS_CHANGED => println(controller.settingsToString)
      case Event.CARDS_CHANGED => println(controller.gameToString)
      case _ =>