package de.htwg.se.set.view

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.util.{Event, Observer}

import scala.swing.*

case class Gui(controller: Controller) extends Frame with Observer:

  title = "SET Game"
  preferredSize = new Dimension(600, 400)
  contents = new Label("Hello, Scala Swing!")
  resizable = false
  centerOnScreen()
  open()

  override def update(event: Event): Unit =
    event match
      case Event.SETTINGS_CHANGED => repaint()
      case Event.CARDS_CHANGED => repaint()
      case Event.SETTINGS_OR_GAME_CHANGED => repaint()
      case _ =>