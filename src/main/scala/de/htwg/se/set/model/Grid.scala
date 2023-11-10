package de.htwg.se.set.model

case class Grid(columns: Int, cards: List[Card], easy: Boolean):

  if columns * 3 != cards.length then
    throw new IllegalArgumentException("Amount of cards has to be equal to the grid size")

  private def legend(columns: Int): String =
    "  " + (65 until 65 + columns).map(_.toChar).map("│" + _ + (" " * (if easy then 2 else 3))).mkString

  private def line(columns: Int): String = "──" + ("┼" + "─" * (if easy then 3 else 4)) * columns

  override def toString: String =
    val result = new StringBuilder(legend(columns) + "\n")
    for rowIndex <- 0 until 3 do
      result.append(line(columns) + "\n")
      result.append((rowIndex + 1) + " ")
      for colIndex <- 0 until columns do
        val cardIndex = rowIndex + 3 * colIndex
        val cardStr = if cardIndex < cards.length then
          if easy then cards(cardIndex).toStringEasy else cards(cardIndex).toString
        else
          " " * (if easy then 3 else 4)
        result.append("│" + cardStr)
      result.append("\n")
    result.toString