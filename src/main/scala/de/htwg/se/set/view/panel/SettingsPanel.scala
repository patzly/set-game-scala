package de.htwg.se.set.view.panel

import de.htwg.se.set.controller.IController
import de.htwg.se.set.controller.controller.base.{ChangePlayerCountAction, StartGameAction, SwitchEasyAction}
import de.htwg.se.set.util.PanelUtil.CompatButton
import de.htwg.se.set.util.{PanelUtil, ResUtil}

import java.awt.{BasicStroke, Color, RenderingHints}
import javax.swing.border.EmptyBorder
import scala.swing.BorderPanel.Position
import scala.swing.{BorderPanel, BoxPanel, Button, Dimension, FlowPanel, Graphics2D, GridBagPanel, GridPanel, Label, Orientation}
import scala.swing.GridBagPanel.{Anchor, Fill}
import scala.swing.event.ButtonClicked

case class SettingsPanel(controller: IController) extends BoxPanel(Orientation.Vertical):

  private val settingsFont = ResUtil.customFont("jost_medium", 26)
  private val padding = 20

  private val playerCountLessButton = new Button:
    reactions += {
      case ButtonClicked(_) => controller.handleAction(ChangePlayerCountAction(controller.settings.playerCount - 1))
    }
    borderPainted = false
    focusPainted = false

    override def paintComponent(g: Graphics2D): Unit = drawArrow(g, size, true, enabled)

  private val playerCountMoreButton = new Button:
    reactions += {
      case ButtonClicked(_) => controller.handleAction(ChangePlayerCountAction(controller.settings.playerCount + 1))
    }
    borderPainted = false
    focusPainted = false

    override def paintComponent(g: Graphics2D): Unit = drawArrow(g, size, false, enabled)

  private val playerCountInputLabel = new Label:
    preferredSize = new Dimension(70, 0)
    font = settingsFont

  private val playerCountInputContainer = new GridPanel(1, 3):
    background = ResUtil.COLOR_BG
    border = EmptyBorder(0, padding, 0, 0)
    contents += playerCountLessButton
    contents += playerCountInputLabel
    contents += playerCountMoreButton

  private val playerCountPanel = new GridPanel(1, 2):
    contents += new BorderPanel:
      background = ResUtil.COLOR_BG
      layout(new Label("PLAYERS") {
        font = settingsFont
      }) = Position.East
    contents += playerCountInputContainer

  private val easyLabelContainer = new BorderPanel:
    background = ResUtil.COLOR_BG
    layout(new Label("EASY MODE") {
      font = settingsFont
    }) = Position.East

  private val easyToggle = new CompatButton:
    font = settingsFont
    borderPainted = false
    reactions += {
      case ButtonClicked(_) =>
        controller.handleAction(SwitchEasyAction())
        update()
    }

  private def easyToggleContainer = new GridPanel(1, 1):
    background = ResUtil.COLOR_BG
    border = EmptyBorder(0, padding, 0, 0)
    contents += easyToggle

  private def easyPanel = new GridPanel(1, 2):
    background = ResUtil.COLOR_BG
    contents += easyLabelContainer
    contents += easyToggleContainer

  background = ResUtil.COLOR_BG
  update()

  private def settingsPanel = new BoxPanel(Orientation.Vertical):
    background = ResUtil.COLOR_BG
    contents += new GridPanel(2, 2):
      background = ResUtil.COLOR_BG
      contents += playerCountPanel
      contents += easyPanel
    contents += new FlowPanel(FlowPanel.Alignment.Center)():
      background = ResUtil.COLOR_BG
      border = EmptyBorder(30, 0, 0, 0)
      contents += new CompatButton("START GAME"):
        reactions += {
          case ButtonClicked(_) => controller.handleAction(StartGameAction())
        }
        font = settingsFont
        foreground = ResUtil.COLOR_BLUE
        background = ResUtil.COLOR_LIGHT
        borderPainted = true
        val paddingHorizontal = 20
        val paddingVertical = 10
        border = new EmptyBorder(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal)

  def update(): Unit =
    contents.clear()
    contents += new GridBagPanel:
      background = ResUtil.COLOR_BG
      val c = new Constraints
      c.fill = Fill.None
      c.weightx = 1
      c.weighty = 1
      c.anchor = Anchor.Center
      layout(settingsPanel) = c
    playerCountLessButton.enabled = controller.settings.playerCount > 1
    playerCountMoreButton.enabled = controller.settings.playerCount < 10
    playerCountInputLabel.text = controller.settings.playerCount.toString
    easyToggle.text = if controller.settings.easy then "ON" else "OFF"
    repaint()
    revalidate()

  private def drawArrow(g: Graphics2D, size: Dimension, pointingLeft: Boolean, enabled: Boolean): Unit =
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    val iconSize = 16
    val w = iconSize / 2
    val h = iconSize
    val x = size.width / 2
    val y = size.height / 2
    g.setColor(if enabled then Color.BLACK else PanelUtil.blendColors(ResUtil.COLOR_BG, Color.BLACK, 0.32))
    g.setStroke(BasicStroke(3))
    if pointingLeft then
      g.drawLine(x - w / 2, y, x + w / 2, y - h / 2)
      g.drawLine(x - w / 2, y, x + w / 2, y + h / 2)
    else
      g.drawLine(x + w / 2, y, x - w / 2, y - h / 2)
      g.drawLine(x + w / 2, y, x - w / 2, y + h / 2)