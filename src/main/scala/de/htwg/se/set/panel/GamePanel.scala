package de.htwg.se.set.panel

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.util.ResUtil

import scala.swing.GridPanel

case class GamePanel(controller: Controller) extends GridPanel(1, 1):

  background = ResUtil.COLOR_BG