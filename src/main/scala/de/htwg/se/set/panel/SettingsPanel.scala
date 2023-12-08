package de.htwg.se.set.panel

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.{ChangePlayerCountAction, StartGameAction, SwitchEasyAction}
import de.htwg.se.set.util.ResUtil

import java.awt.{BasicStroke, Color, RenderingHints}
import javax.swing.border.{CompoundBorder, MatteBorder}
import scala.swing.Swing.EmptyBorder
import scala.swing.event.ButtonClicked
import scala.swing.*

case class SettingsPanel(controller: Controller) extends GridPanel(3, 1):

  private val settingsFont = ResUtil.customFont("jost_medium", 36)

  private val playerCountLabelContainer = new GridPanel(1, 1):
    background = ResUtil.COLOR_BG
    border = new MatteBorder(0, 0, 0, 1, Color.BLACK)
    contents += new Label("PLAYERS"):
      font = settingsFont
      
  private val playerCountLessButton = new Button():
    reactions += {
      case ButtonClicked(_) => controller.handleAction(ChangePlayerCountAction(controller.settings.playerCount - 1))
    }
    borderPainted = false
    focusPainted = false

    override def paintComponent(g: Graphics2D): Unit = drawArrow(g, size, true)
    
  private val playerCountMoreButton = new Button():
    reactions += {
      case ButtonClicked(_) => controller.handleAction(ChangePlayerCountAction(controller.settings.playerCount + 1))
    }
    borderPainted = false
    focusPainted = false

    override def paintComponent(g: Graphics2D): Unit = drawArrow(g, size, false)
    
  private val playerCountInputLabel = new Label(""):
    font = settingsFont
    
  private val playerCountInputContainer = new GridPanel(1, 3):
    background = ResUtil.COLOR_BG
    private val innerBorder = EmptyBorder(0, 40, 0, 40)
    private val outerBorder = new MatteBorder(0, 1, 0, 0, Color.BLACK)
    border = new CompoundBorder(outerBorder, innerBorder)
    contents += playerCountLessButton
    contents += playerCountInputLabel
    contents += playerCountMoreButton
    
  private val playerCountPanel = new GridPanel(1, 2):
    contents += playerCountLabelContainer
    contents += playerCountInputContainer
    border = new MatteBorder(0, 0, 2, 0, Color.BLACK)

  private val easyLabelContainer = new GridPanel(1, 1):
    background = ResUtil.COLOR_BG
    border = new MatteBorder(0, 0, 0, 1, Color.BLACK)
    contents += new Label("MODE"):
      font = settingsFont
      
  private val easyToggle: ToggleButton = new ToggleButton(""):
    reactions += {
      case ButtonClicked(_) => controller.handleAction(SwitchEasyAction)
    }
    font = settingsFont
    borderPainted = false
    focusPainted = false
    
  private val easyToggleContainer = new GridPanel(1, 1):
    border = new MatteBorder(0, 1, 0, 0, Color.BLACK)
    contents += easyToggle
    
  private val easyPanel = new GridPanel(1, 2):
    contents += easyLabelContainer
    contents += easyToggleContainer
    border = new MatteBorder(0, 0, 2, 0, Color.BLACK)

  background = ResUtil.COLOR_LIGHT
  contents += playerCountPanel
  contents += easyPanel
  contents += new Button("START GAME"):
    reactions += {
      case ButtonClicked(_) => controller.handleAction(StartGameAction)
    }
    foreground = ResUtil.COLOR_BLUE
    font = settingsFont
    borderPainted = false
    focusPainted = false

  def update(): Unit =
    playerCountInputLabel.text = controller.settings.playerCount.toString
    playerCountMoreButton.enabled = controller.settings.playerCount < 10
    playerCountLessButton.enabled = controller.settings.playerCount > 1
    easyToggle.text = if controller.settings.easy then "EASY" else "NORMAL"
    easyToggleContainer.background = if controller.settings.easy then ResUtil.COLOR_YELLOW else ResUtil.COLOR_ORANGE

  private def drawArrow(g: Graphics2D, size: Dimension, pointingLeft: Boolean): Unit =
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    val w = 10
    val h = 20
    val x = size.width / 2
    val y = size.height / 2
    val iconSize = 20
    g.setColor(java.awt.Color.BLACK)
    g.setStroke(new BasicStroke(4))
    if pointingLeft then
      g.drawLine(x - w / 2, y, x + w / 2, y - h / 2)
      g.drawLine(x - w / 2, y, x + w / 2, y + h / 2)
    else
      g.drawLine(x + w / 2, y, x - w / 2, y - h / 2)
      g.drawLine(x + w / 2, y, x - w / 2, y + h / 2)