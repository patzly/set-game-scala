package de.htwg.se.set.view

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.panel.{GamePanel, MenuPanel, SettingsPanel}
import de.htwg.se.set.util.{Event, Observer}

import scala.swing.*

case class Gui(controller: Controller) extends Frame with Observer:

  controller.add(this)

  title = "SET Game"
  preferredSize = new Dimension(800, 600)
  resizable = false

  private val menuPanel = MenuPanel(controller)
  private val settingsPanel = SettingsPanel(controller)
  private val gamePanel = GamePanel(controller)

  menuPanel.update()
  settingsPanel.update()
  contents = frame

  centerOnScreen()
  open()

  override def update(event: Event): Unit =
    event match
      case Event.SETTINGS_CHANGED => settingsPanel.update()
      case Event.CARDS_CHANGED => repaint()
      case Event.SETTINGS_OR_GAME_CHANGED => settingsPanel.update()
      case Event.IN_GAME_CHANGED => contents = frame
      case _ =>
    menuPanel.update()

  private def frame = new BorderPanel:
    layout(menuPanel) = BorderPanel.Position.North
    layout(if controller.settings.inGame then gamePanel else settingsPanel) = BorderPanel.Position.Center