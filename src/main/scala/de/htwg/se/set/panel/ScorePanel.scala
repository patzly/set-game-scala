package de.htwg.se.set.panel

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.ExitAction
import de.htwg.se.set.util.PanelUtil.CompatButton
import de.htwg.se.set.util.ResUtil

import javax.swing.border.EmptyBorder
import scala.swing.*
import scala.swing.BorderPanel.Position
import scala.swing.GridBagPanel.{Anchor, Fill}
import scala.swing.event.ButtonClicked

case class ScorePanel(controller: Controller) extends BoxPanel(Orientation.Vertical):

  private val scoreFont = ResUtil.customFont("jost_medium", 26)

  background = ResUtil.COLOR_BG
  update()

  private def scorePanel = new BoxPanel(Orientation.Vertical):
    background = ResUtil.COLOR_BG
    if controller.settings.singlePlayer then
      contents += new FlowPanel(FlowPanel.Alignment.Center)():
        background = ResUtil.COLOR_BG
        contents += new Label("ALL SETS FOUND!"):
          font = scoreFont
    else
      contents += new GridPanel(controller.game.players.size, 2):
        background = ResUtil.COLOR_BG
        controller.game.players.foreach(player =>
          contents += new BorderPanel:
            background = ResUtil.COLOR_BG
            layout(new Label("Player " + player.number) {
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
      border = EmptyBorder(30, 0, 0, 0)
      contents += new CompatButton("FINISH"):
        reactions += {
          case ButtonClicked(_) => controller.handleAction(ExitAction)
        }
        font = scoreFont
        foreground = ResUtil.COLOR_BLUE
        background = ResUtil.COLOR_LIGHT
        borderPainted = true
        val paddingHorizontal = 20
        val paddingVertical = 10
        border = EmptyBorder(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal)

  def update(): Unit =
    contents.clear()
    contents += new GridBagPanel:
      background = ResUtil.COLOR_BG
      val c = new Constraints
      c.fill = Fill.None
      c.weightx = 1
      c.weighty = 1
      c.anchor = Anchor.Center
      layout(scorePanel) = c