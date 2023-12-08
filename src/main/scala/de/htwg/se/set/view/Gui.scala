package de.htwg.se.set.view

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.panel.{GamePanel, MenuPanel, SettingsPanel}
import de.htwg.se.set.util.{Event, Observer}

import javax.swing.UIManager
import scala.swing.{BorderPanel, Dimension, Frame}

case class Gui(controller: Controller) extends Frame with Observer:

  controller.add(this)

  title = "SET Game"
  preferredSize = new Dimension(900, 600)
  resizable = false
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
  System.setProperty("awt.useSystemAAFontSettings", "on")
  System.setProperty("swing.aatext", "true")

  private val menuPanel = MenuPanel(controller)
  private val settingsPanel = SettingsPanel(controller)
  private val gamePanel = GamePanel(controller)

  menuPanel.update()
  settingsPanel.update()
  gamePanel.update()
  contents = frame

  centerOnScreen()
  open()

  override def update(event: Event): Unit =
    event match
      case Event.SETTINGS_CHANGED => settingsPanel.update()
      case Event.CARDS_CHANGED => gamePanel.update()
      case Event.SETTINGS_OR_GAME_CHANGED =>
        settingsPanel.update()
        gamePanel.update()
      case Event.PLAYERS_CHANGED => gamePanel.update()
      case Event.IN_GAME_CHANGED => contents = frame
      case _ =>
    menuPanel.update()

  private def frame = new BorderPanel:
    layout(menuPanel) = BorderPanel.Position.North
    layout(if controller.settings.inGame then gamePanel else settingsPanel) = BorderPanel.Position.Center