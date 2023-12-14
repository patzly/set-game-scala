package de.htwg.se.set.view

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.GameMode.{GAME_END, IN_GAME, SETTINGS}
import de.htwg.se.set.panel.{GamePanel, MenuPanel, ScorePanel, SettingsPanel}
import de.htwg.se.set.util.{Event, Observer}

import javax.swing.UIManager
import javax.swing.plaf.nimbus.NimbusLookAndFeel
import scala.swing.{BorderPanel, Dimension, Frame}

case class Gui(controller: Controller) extends Frame with Observer:

  controller.add(this)

  title = "SET Game"
  preferredSize = new Dimension(1000, 600)
  resizable = false
  UIManager.setLookAndFeel(new NimbusLookAndFeel)
  System.setProperty("awt.useSystemAAFontSettings", "on")
  System.setProperty("swing.aatext", "true")

  private val menuPanel = MenuPanel(controller)
  private val settingsPanel = SettingsPanel(controller)
  private val gamePanel = GamePanel(controller)
  private val scorePanel = ScorePanel(controller)

  contents = content

  centerOnScreen()
  open()

  override def update(event: Event): Unit =
    event match
      case Event.SETTINGS_CHANGED => settingsPanel.update()
      case Event.CARDS_CHANGED => gamePanel.update()
      case Event.SETTINGS_OR_GAME_CHANGED =>
        settingsPanel.update()
        gamePanel.update()
      case Event.STATE_CHANGED =>
        gamePanel.update()
      case Event.MESSAGE_CHANGED =>
        gamePanel.update()
      case Event.PLAYERS_CHANGED => gamePanel.update()
      case Event.GAME_MODE_CHANGED =>
        scorePanel.update()
        contents = content
      case _ =>
    menuPanel.update()

  private def content = new BorderPanel:
    layout(menuPanel) = BorderPanel.Position.North
    private val panel = controller.settings.mode match
      case SETTINGS => settingsPanel
      case IN_GAME => gamePanel
      case GAME_END => scorePanel
    layout(panel) = BorderPanel.Position.Center