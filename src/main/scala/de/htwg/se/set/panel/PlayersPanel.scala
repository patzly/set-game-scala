package de.htwg.se.set.panel

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.{Player, SelectPlayerAction}
import de.htwg.se.set.util.PanelUtil.CompatButton
import de.htwg.se.set.util.ResUtil

import javax.swing.border.EmptyBorder
import scala.swing.FlowPanel
import scala.swing.event.ButtonClicked

class PlayersPanel(controller: Controller) extends FlowPanel(FlowPanel.Alignment.Center)():

  private val margin = 30
  private val paddingHorizontal = 20
  private val paddingVertical = 10
  private val playerFont = ResUtil.customFont("jost_medium", 26)

  background = ResUtil.COLOR_LIGHT
  border = EmptyBorder(margin, 0, margin, 0)
  update()

  def update(): Unit =
    for player <- controller.game.players do
      val text = if player.singlePlayer then
        player.sets.length + " OF " + (if player.easy then 3 else 6) + " SETS FOUND"
      else
        "PLAYER " + player.number + ": " + player.sets.length
      contents += new CompatButton(text):
        background = ResUtil.COLOR_LIGHT
        foreground = ResUtil.COLOR_BLUE
        borderPainted = controller.game.selectedPlayer match
          case Some(p) => p.number == player.number && !player.singlePlayer
          case None => false
        font = playerFont
        border = new EmptyBorder(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal)
        reactions += {
          case ButtonClicked(_) =>
            val select = controller.game.selectedPlayer match
              case Some(p) => false
              case None => true
            if select then controller.handleAction(SelectPlayerAction(player.number))
        }