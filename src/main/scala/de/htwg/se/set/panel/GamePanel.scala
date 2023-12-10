package de.htwg.se.set.panel

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.util.ResUtil

import java.awt.Color
import javax.swing.border.MatteBorder
import scala.swing.*
import scala.swing.BorderPanel.Position
import scala.swing.GridBagPanel.{Anchor, Fill}

case class GamePanel(controller: Controller) extends BoxPanel(Orientation.Vertical):
  
  private val rows = 3

  background = ResUtil.COLOR_BG
  update()

  def update(): Unit =
    val tableCards = controller.game.tableCards
    val columns = tableCards.length / rows
    val cardsPanel: GridBagPanel = new GridBagPanel:
      background = ResUtil.COLOR_BG
      border = MatteBorder(0, 0, 2, 0, Color.BLACK)
      val c = new Constraints
      c.fill = Fill.None
      c.weightx = 1
      c.weighty = 1
      c.anchor = Anchor.Center
      layout(CardsPanel(controller, rows, columns)) = c
    contents.clear()
    contents += new BorderPanel:
      background = ResUtil.COLOR_BG
      layout(cardsPanel) = Position.Center
      layout(PlayersPanel(controller)) = Position.South
      if controller.settings.singlePlayer then
        layout(SetsPanel(controller)) = Position.East
    repaint()
    revalidate()