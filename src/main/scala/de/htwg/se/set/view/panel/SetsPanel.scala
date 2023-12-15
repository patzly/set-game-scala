package de.htwg.se.set.view.panel

import de.htwg.se.set.controller.controller.Controller
import de.htwg.se.set.util.ResUtil

import java.awt.Color
import javax.swing.border.MatteBorder
import scala.swing.*
import scala.swing.GridBagPanel.{Anchor, Fill}

case class SetsPanel(controller: Controller) extends BoxPanel(Orientation.Vertical):

  private val rows = 3

  preferredSize = new Dimension(300, 0)
  background = ResUtil.COLOR_BG
  update()

  def update(): Unit =
    if controller.game.players.isEmpty then return
    val sets = controller.game.players.head.sets
    val max = if controller.settings.easy then 3 else 6
    val setsGrid = new GridPanel(max, 3):
      background = ResUtil.COLOR_BG
      for set <- sets do
        for card <- List(set.card1, set.card2, set.card3) do
          contents += CardButton(Some(card), true)
      for _ <- 1 to (max - sets.length) do
        for _ <- 1 to 3 do
          contents += CardButton(None, true)
    contents.clear()
    contents += new GridBagPanel:
      background = ResUtil.COLOR_BG
      border = new MatteBorder(0, 2, 2, 0, Color.BLACK)
      val c = new Constraints
      c.fill = Fill.None
      c.weightx = 1
      c.weighty = 1
      c.anchor = Anchor.Center
      layout(setsGrid) = c
    repaint()
    revalidate()