package de.htwg.se.set.model

sealed trait Grid(columns: Int, cards: List[Card]):

  if columns * 3 != cards.length then
    throw new IllegalArgumentException("Amount of cards has to be equal to the grid size")
  
  def columnWidth: Int

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
        val cardStr = if cardIndex < cards.length then
          cards(cardIndex).toString
        else
          " " * columnWidth
        result.append("│" + cardStr)
      result.append("\n")
    result.toString

object Grid:

  private class NormalGrid(columns: Int, cards: List[Card]) extends Grid(columns, cards):

    override def columnWidth: Int = 4

  private class EasyGrid(columns: Int, cards: List[Card]) extends Grid(columns, cards):

    override def columnWidth: Int = 3

  def apply(columns: Int, cards: List[Card], easy: Boolean): Grid =
    if easy then EasyGrid(columns, cards) else NormalGrid(columns, cards)