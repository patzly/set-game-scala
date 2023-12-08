package de.htwg.se.set.panel

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.{FinishAction, UndoAction}
import de.htwg.se.set.util.ResUtil

import java.awt.{BasicStroke, Color, RenderingHints}
import javax.swing.border.{EmptyBorder, MatteBorder}
import scala.swing.*
import scala.swing.BorderPanel.Position
import scala.swing.GridBagPanel.{Anchor, Fill}
import scala.swing.event.ButtonClicked

case class ScorePanel(controller: Controller) extends BoxPanel(Orientation.Vertical):

  private val scoreFont = ResUtil.customFont("jost_medium", 32)
  private val buttonFont = ResUtil.customFont("jost_medium", 26)
  private val cornerRadius = 30
  private val paddingHorizontal = 20
  private val paddingVertical = 10

  background = ResUtil.COLOR_BG
  update()

  def update(): Unit =
    contents.clear()
    contents += new GridBagPanel:
      background = ResUtil.COLOR_BG
      val c = new Constraints
      c.fill = Fill.None
      c.weightx = 1
      c.weighty = 1
      c.anchor = Anchor.Center
      private val boxPanel = new BoxPanel(Orientation.Vertical):
        background = ResUtil.COLOR_BG
        contents += new GridPanel(controller.game.players.size, 2):
          background = ResUtil.COLOR_BG
          controller.game.players.foreach(player =>
            contents += new BorderPanel:
              background = ResUtil.COLOR_BG
              layout(new Label("Player " + player.number + ":") {
                font = scoreFont
              }) = Position.West
            contents += new BorderPanel:
              background = ResUtil.COLOR_BG
              layout(new Label(player.sets.length.toString) {
                font = scoreFont
                foreground = ResUtil.COLOR_RED
              }) = Position.East
          )
        contents += new FlowPanel(FlowPanel.Alignment.Center)():
          background = ResUtil.COLOR_BG
          border = new EmptyBorder(20, 0, 0, 0)
          contents += new Button("FINISH"):
            reactions += {
              case ButtonClicked(_) => controller.handleAction(FinishAction)
            }
            font = buttonFont
            foreground = Color.BLACK
            borderPainted = false
            focusPainted = false
            border = new EmptyBorder(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal)

            override def paintComponent(g: Graphics2D): Unit =
              super.paintComponent(g)
              g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
              val stroke = 2
              g.setColor(Color.BLACK)
              g.setStroke(new BasicStroke(stroke))
              g.drawRoundRect(stroke, stroke, size.width - stroke * 2, size.height - stroke * 2, cornerRadius, cornerRadius)

      layout(boxPanel) = c

    repaint()
    revalidate()