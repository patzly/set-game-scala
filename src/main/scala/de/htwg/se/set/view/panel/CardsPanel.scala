package de.htwg.se.set.view.panel

import de.htwg.se.set.controller.IController
import de.htwg.se.set.controller.controller.SelectCardsAction
import de.htwg.se.set.model.{Color, ICard, Shading, Symbol}
import de.htwg.se.set.util.{PanelUtil, ResUtil}

import java.awt.{BasicStroke, Polygon, RenderingHints, TexturePaint}
import java.awt.geom.{AffineTransform, Path2D, Rectangle2D, RoundRectangle2D}
import java.awt.image.BufferedImage
import javax.swing.border.EmptyBorder
import scala.swing.{Button, Dimension, Graphics2D, GridPanel}
import scala.swing.event.ButtonClicked

class CardsPanel(controller: IController, rows: Int, columns: Int) extends GridPanel(rows, columns):

  private val margin = 5
  private val tableCards = controller.game.tableCards
  private val cardsGrid = Array.ofDim[CardButton](rows, columns)

  background = ResUtil.COLOR_BG
  border = EmptyBorder(margin, margin, margin, margin)

  for index <- tableCards.indices do
    val col = index / rows
    val row = index % rows
    val card = tableCards(index)
    cardsGrid(row)(col) = CardButton(Some(card))
  for {
    row <- 0 until rows
    col <- 0 until columns
    if cardsGrid(row)(col) != null
  } {
    val cardButton = cardsGrid(row)(col)
    cardButton.reactions += {
      case ButtonClicked(_) =>
        val select = controller.game.selectedPlayer match
          case Some(p) => true
          case None => false
        if !select then
          controller.setMessage("Selection of player required.")
        cardButton.selected = select && !cardButton.selected
        repaint()
        revalidate()
        if selectedIndices.size == 3 then
          val selectedCoordinates = selectedIndices.map(index => {
            val col = index % columns
            val row = index / columns
            (col + 'A').toChar.toString + (row + 1).toString
          }).toList
          controller.handleAction(SelectCardsAction(selectedCoordinates))
    }
    contents += cardButton
  }
  repaint()
  revalidate()

  private def selectedIndices =
    for {
      (cardButton, index) <- contents.zipWithIndex
      if cardButton.isInstanceOf[CardButton] && cardButton.asInstanceOf[CardButton].selected
    } yield index

class CardButton(card: Option[ICard], small: Boolean = false) extends Button:

  private val outerMargin = if small then 6 else 8
  private val cardRadius = if small then 20 else 30
  private val outlineStrokeWidth = if small then 2 else 3

  preferredSize = new Dimension(if small then 80 else 150, if small then 57 else 100)

  override def paintComponent(g: Graphics2D): Unit =
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    val x = outerMargin
    val y = outerMargin
    val w = size.width - outerMargin - x
    val h = size.height - outerMargin - y
    card match
      case Some(c) => drawCard(g, c, x, y, w, h)
      case None => drawPlaceHolder(g, x, y, w, h)

  private def drawPlaceHolder(g: Graphics2D, x: Int, y: Int, width: Int, height: Int): Unit =
    g.setColor(PanelUtil.blendColors(ResUtil.COLOR_BG, java.awt.Color.BLACK, 0.08))
    g.fill(RoundRectangle2D.Double(x, y, width, height, cardRadius, cardRadius))

  private def drawCard(g: Graphics2D, card: ICard, x: Int, y: Int, width: Int, height: Int): Unit =
    g.setColor(java.awt.Color.WHITE)
    g.fill(RoundRectangle2D.Double(x, y, width, height, cardRadius, cardRadius))
    g.setColor(if selected then ResUtil.COLOR_ORANGE else java.awt.Color.BLACK)
    g.setStroke(BasicStroke(if selected then 3 else 2))
    g.draw(RoundRectangle2D.Double(x, y, width, height, cardRadius, cardRadius))

    val symbolColor = card.color match
      case Color.RED => ResUtil.COLOR_RED
      case Color.GREEN => ResUtil.COLOR_YELLOW
      case Color.BLUE => ResUtil.COLOR_BLUE
    g.setColor(symbolColor)
    val drawingStrategy = card.symbol match
      case Symbol.OVAL => OvalDrawingStrategy(g)
      case Symbol.SQUIGGLE => SquiggleDrawingStrategy(g)
      case Symbol.DIAMOND => DiamondDrawingStrategy(g)
    card.shading match
      case Shading.SOLID =>
        drawSymbols(card.number, drawingStrategy, true)
      case Shading.OUTLINED =>
        g.setStroke(BasicStroke(outlineStrokeWidth))
        drawSymbols(card.number, drawingStrategy, false)
      case Shading.STRIPED =>
        g.setStroke(BasicStroke(outlineStrokeWidth))
        drawSymbols(card.number, drawingStrategy, false)
        g.setPaint(stripedTexturePaint(symbolColor))
        drawSymbols(card.number, drawingStrategy, true)

  private def drawSymbols(number: Int, strategy: DrawingStrategy, solid: Boolean): Unit =
    val w = size.width - outerMargin * 2
    val h = size.height - outerMargin * 2
    val symbolWidth = w / 5
    val symbolHeight = h / 2
    val positions = number match
      case 1 => Array(w / 2 + outerMargin)
      case 2 =>
        val space = w / 4
        val extra = (space - symbolWidth) / 2
        Array(space + symbolWidth / 2 + outerMargin + extra, 2 * space + symbolWidth / 2 + outerMargin + extra)
      case 3 =>
        val space = w / 4
        Array(space + outerMargin,
          2 * space + outerMargin,
          3 * space + outerMargin)
    for (x <- positions) strategy.draw(x, h / 2 + outerMargin - symbolHeight / 2, symbolWidth, symbolHeight, solid)

  private def stripedTexturePaint(color: java.awt.Color): TexturePaint =
    val strokeSpacing = 3
    val patternSize = 200
    val pattern = BufferedImage(patternSize, patternSize, BufferedImage.TYPE_INT_ARGB)
    val pg = pattern.createGraphics()
    pg.setColor(color)
    pg.setStroke(BasicStroke(outlineStrokeWidth))
    pg.setBackground(java.awt.Color(0, 0, 0, 0))
    pg.clearRect(0, 0, patternSize, patternSize)
    var y = 0
    while (y < patternSize)
      pg.drawLine(0, y, patternSize, y)
      y += outlineStrokeWidth + strokeSpacing
    pg.dispose()
    TexturePaint(pattern, Rectangle2D.Double(0, 0, patternSize, patternSize))

  private trait DrawingStrategy:

    def draw(x: Int, y: Int, w: Int, h: Int, solid: Boolean): Unit

  private class OvalDrawingStrategy(g: Graphics2D) extends DrawingStrategy:

    override def draw(x: Int, y: Int, w: Int, h: Int, solid: Boolean): Unit =
      val rectX = x - w / 2
      val rectY = y
      val radius = w
      if solid then
        g.fillRoundRect(rectX, rectY, w, h, radius, radius)
      else
        g.drawRoundRect(rectX, rectY, w, h, radius, radius)

  private class DiamondDrawingStrategy(g: Graphics2D) extends DrawingStrategy:

    override def draw(x: Int, y: Int, w: Int, h: Int, solid: Boolean): Unit =
      val diamond = Polygon()
      diamond.addPoint(x, y)
      diamond.addPoint(x - w / 2, y + h / 2)
      diamond.addPoint(x, y + h)
      diamond.addPoint(x + w / 2, y + h / 2)
      if solid then g.fillPolygon(diamond) else g.drawPolygon(diamond)

  private class SquiggleDrawingStrategy(g: Graphics2D) extends DrawingStrategy:

    override def draw(x: Int, y: Int, w: Int, h: Int, solid: Boolean): Unit =
      val squiggle = Path2D.Double()
      squiggle.moveTo(0, 0)
      squiggle.curveTo(8.4, 21.9, -14.3, 45.8, -41.0, 39.0)
      squiggle.curveTo(-51.7, 36.3, -61.8, 27.0, -77.0, 38.0)
      squiggle.curveTo(-94.4, 50.6, -98.6, 43.3, -99.0, 25.0)
      squiggle.curveTo(-99.4, 7.0, -84.9, -5.3, -68.0, -3.0)
      squiggle.curveTo(-44.8, -0.8, -42.1, 16.5, -15.0, -1.0)
      squiggle.curveTo(-8.7, -5.0, -3.1, -8.1, 0, 0)
      squiggle.closePath()
      val atScale = AffineTransform()
      val scale = if small then 0.2 else 0.41
      atScale.scale(scale, scale)
      val transformedScale = atScale.createTransformedShape(squiggle)
      val transformedCenterX = transformedScale.getBounds.getCenterX
      val transformedCenterY = transformedScale.getBounds.getCenterY
      val atTranslation = AffineTransform()
      atTranslation.translate(x + (if small then 10 else 20), y + (if small then 9 else 15))
      atTranslation.rotate(Math.toRadians(90), transformedCenterX, transformedCenterY)
      val transformedTranslation = atTranslation.createTransformedShape(transformedScale)
      if solid then
        g.fill(transformedTranslation)
      else
        g.draw(transformedTranslation)