package de.htwg.se.set.model

class Grid(val rows : Int, val columns: Int, val cards: List[Card], val easy: Boolean):

  private def legend(columns: Int): String =
    " " + (65 until 65 + columns).map(_.toChar).map("│" + _ + (if easy then "  " else "   ")).mkString

  private def line(columns: Int): String =
    if easy then "─" + "┼───" * columns else "─" + "┼────" * columns

  override def toString: String =
    val iterator = cards.iterator
    var result = legend(columns) + "\n"
    for index <- 0 until rows do
      result += line(columns) + "\n"
      result += index + 1
      result += (1 to columns).map(_ =>
        if iterator.hasNext then
          val card = iterator.next()
          "│" + (if easy then card.toStringEasy else card.toString)
        else "│"
      ).mkString + "\n"
    result