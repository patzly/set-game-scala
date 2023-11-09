package de.htwg.se.set.model

case class Grid(rows : Int, columns: Int, cards: List[Card], easy: Boolean):

  private def legend(columns: Int): String =
    "  " + (65 until 65 + columns).map(_.toChar).map("│" + _ + (" " * (if easy then 2 else 3))).mkString

  private def line(columns: Int): String = "──" + ("┼" + "─" * (if easy then 3 else 4)) * columns

  override def toString: String =
    println(cards.mkString(", "))
    val result = new StringBuilder(legend(columns) + "\n")
    for rowIndex <- 0 until rows do
      result.append(line(columns) + "\n")
      result.append((rowIndex + 1) + " ")
      for colIndex <- 0 until columns do
        val cardIndex = rowIndex + rows * colIndex
        val cardStr = if cardIndex < cards.length then
          if easy then cards(cardIndex).toStringEasy else cards(cardIndex).toString
        else
          " " * (if easy then 3 else 4)
        result.append("│" + cardStr)
      result.append("\n")
    result.toString