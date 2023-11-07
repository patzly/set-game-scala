package de.htwg.se.set.model

case class Grid(rows : Int, columns: Int, cards: List[Card], easy: Boolean):

  private def legend(columns: Int): String =
    "  " + (65 until 65 + columns).map(_.toChar).map("│" + _ + (" " * (if easy then 2 else 3))).mkString

  private def line(columns: Int): String = "──" + ("┼" + "─" * (if easy then 3 else 4)) * columns

  override def toString: String =
    val cardMatrix = Array.ofDim[Option[Card]](rows, columns)
    cards.zipWithIndex.foreach { case (card, index) =>
      val row = index % rows
      val column = index / rows
      cardMatrix(row)(column) = Some(card)
    }
    var result = legend(columns) + "\n"
    for rowIndex <- 0 until rows do
      result += line(columns) + "\n"
      result += (rowIndex + 1) + " "
      for colIndex <- 0 until columns do
        val cardStr = cardMatrix(rowIndex)(colIndex).map { card =>
          if easy then card.toStringEasy else card.toString
        }.getOrElse(" " * (if easy then 3 else 4))
        result += "│" + cardStr
      result += "\n"
    result