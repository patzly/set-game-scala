package de.htwg.se.set.util

import java.awt.{Color, GraphicsEnvironment}
import scala.swing.Font

object ResUtil:

  val COLOR_BG: Color = Color.decode("#e9e3d3")
  val COLOR_RED: Color = Color.decode("#da0100")
  val COLOR_ORANGE: Color = Color.decode("#f9520a")
  val COLOR_YELLOW: Color = Color.decode("#ffa800")
  val COLOR_BLUE: Color = Color.decode("#060488")
  val COLOR_LIGHT: Color = Color.decode("#f4f1b4")

  def customFont(name: String, size: Int): Font =
    val stream = getClass.getResourceAsStream("/font/" + name + ".ttf")
    val font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, stream).deriveFont(java.awt.Font.PLAIN, size)
    GraphicsEnvironment.getLocalGraphicsEnvironment.registerFont(font)
    font