package de.htwg.se.set.model

class Grid(val rows : Int, val columns: Int, val cards: List[Card], val selected: List[Boolean], val easy: Boolean):

  private def legend(columns: Int): String =
    " " + (65 until 65 + columns).map(_.toChar).map("│" + _ + (if easy then "  " else "   ")).mkString

  private def line(columns: Int): String =
    if easy then "─" + "┼───" * columns else "─" + "┼────" * columns
  
  private def underline(text: String): String = "\u001b[4m" + text + "\u001b[24m"

  override def toString: String =
    val iterator = cards.zip(selected).iterator
    var result = legend(columns) + "\n"
    for index <- 0 until rows do
      result += line(columns) + "\n"
      result += index + 1
      result += (1 to columns).map(_ =>
        if iterator.hasNext then
          val tuple = iterator.next()
          val card = if easy then tuple._1.toStringEasy else tuple._1.toString
          val selected = tuple._2
          if selected then "│" + underline(card) else "│" + card
        else "│"
      ).mkString + "\n"
    result