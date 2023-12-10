package de.htwg.se.set.util

import java.awt.{BasicStroke, Color, RenderingHints}
import scala.swing.{Button, Graphics2D}

object PanelUtil:

  class CompatButton(label: String = "") extends Button(label):

    private val cornerRadius = 30
    private val strokeWidth = 2

    foreground = Color.BLACK
    background = ResUtil.COLOR_BG
    borderPainted = false
    focusPainted = false

    override protected def paintComponent(g: Graphics2D): Unit =
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
      if borderPainted then
        val width = size.width - strokeWidth * 2
        val height = size.height - strokeWidth * 2
        g.setColor(background)
        g.fillRoundRect(strokeWidth, strokeWidth, width, height, cornerRadius, cornerRadius)
        g.setColor(Color.BLACK)
        g.setStroke(new BasicStroke(strokeWidth))
        g.drawRoundRect(strokeWidth, strokeWidth, width, height, cornerRadius, cornerRadius)
      if text.nonEmpty then
        g.setColor(blendColors(background, foreground, if enabled then 1 else 0.32))
        val metrics = g.getFontMetrics(font)
        val x = (size.width - metrics.stringWidth(text)) / 2
        val y = ((size.height - metrics.getHeight) / 2) + metrics.getAscent
        g.drawString(text, x, y)

    override def text_=(text: String): Unit =
      super.text = text
      revalidate()
      repaint()

  def blendColors(color1: Color, color2: Color, fraction: Double): Color =
    val r = (color1.getRed * (1 - fraction) + color2.getRed * fraction).toInt
    val g = (color1.getGreen * (1 - fraction) + color2.getGreen * fraction).toInt
    val b = (color1.getBlue * (1 - fraction) + color2.getBlue * fraction).toInt
    val a = (color1.getAlpha * (1 - fraction) + color2.getAlpha * fraction).toInt
    new Color(r, g, b, a)