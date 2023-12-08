package de.htwg.se.set.panel

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.{Player, SelectPlayerAction}
import de.htwg.se.set.util.ResUtil

import java.awt.{BasicStroke, Color, RenderingHints}
import javax.swing.border.EmptyBorder
import scala.swing.event.ButtonClicked
import scala.swing.{Button, FlowPanel, Graphics2D}

class PlayersPanel(controller: Controller) extends FlowPanel(FlowPanel.Alignment.Center)():

  private val margin = 30

  background = ResUtil.COLOR_LIGHT
  border = new EmptyBorder(margin, 0, margin, 0)

  for player <- controller.game.players do
    contents += new PlayerButton(player):
      selected = controller.game.selectedPlayer match
        case Some(p) => p.number == player.number
        case _ => false
      reactions += {
        case ButtonClicked(_) =>
          val select = controller.game.selectedPlayer match
            case Some(p) => false
            case None => true
          if select then controller.handleAction(SelectPlayerAction(player.number))
      }

  repaint()
  revalidate()

  private class PlayerButton(player: Player) extends Button:

    private val cornerRadius = 30
    private val paddingHorizontal = 20
    private val paddingVertical = 10

    text = if player.singlePlayer then
      player.sets.length + " of " + (if player.easy then 3 else 6) + " SETs found"
    else
      "Player " + player.number + ": " + player.sets.length
    font = ResUtil.customFont("jost_medium", 26)
    foreground = Color.BLACK
    borderPainted = false
    focusPainted = false
    border = new EmptyBorder(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal)

    override def paintComponent(g: Graphics2D): Unit =
      super.paintComponent(g)
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
      if selected && !player.singlePlayer then
        val stroke = 2
        g.setColor(Color.BLACK)
        g.setStroke(new BasicStroke(stroke))
        g.drawRoundRect(stroke, stroke, size.width - stroke * 2, size.height - stroke * 2, cornerRadius, cornerRadius)