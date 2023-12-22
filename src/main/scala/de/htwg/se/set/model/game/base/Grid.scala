package de.htwg.se.set.model.game.base

import de.htwg.se.set.model.{ICard, IGrid}

case class Grid(columns: Int, cards: List[ICard], easy: Boolean) extends IGrid:

  if columns * 3 != cards.length then
    throw new IllegalArgumentException("Amount of cards has to be equal to the grid size")

  private def columnWidth: Int = if easy then 3 else 4

  private def legend(columns: Int): String =
    "  " + (65 until 65 + columns).map(_.toChar).map("│" + _ + (" " * (columnWidth - 1))).mkString

  private def line(columns: Int): String = "──" + ("┼" + "─" * columnWidth) * columns

  override def toString: String =
    val result = new StringBuilder(legend(columns) + "\n")
    for rowIndex <- 0 until 3 do
      result.append(line(columns) + "\n")
      result.append((rowIndex + 1) + " ")
      for colIndex <- 0 until columns do
        val cardIndex = rowIndex + 3 * colIndex
        result.append("│" + (if cardIndex < cards.length then cards(cardIndex).toString else " " * columnWidth))
      result.append("\n")
    result.toString