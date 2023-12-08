package de.htwg.se.set.panel

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.util.ResUtil

import java.awt.Color
import javax.swing.border.MatteBorder
import scala.swing.*
import scala.swing.GridBagPanel.{Anchor, Fill}

case class GamePanel(controller: Controller) extends BoxPanel(Orientation.Vertical):

  private val margin = 5
  private val rows = 3

  background = ResUtil.COLOR_BG
  update()

  def update(): Unit =
    val tableCards = controller.game.tableCards
    val columns = tableCards.length / rows
    contents.clear()
    contents += new GridBagPanel:
      background = ResUtil.COLOR_BG
      border = new MatteBorder(0, 0, 2, 0, Color.BLACK)
      val c = new Constraints
      c.fill = Fill.None
      c.weightx = 1.0
      c.weighty = 1.0
      c.anchor = Anchor.Center
      layout(new CardsPanel(controller, rows, columns)) = c
    contents += new PlayersPanel(controller)
    repaint()
    revalidate()