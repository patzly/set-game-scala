package de.htwg.se.set.model

import de.htwg.se.set.util.PrintUtil

case class Card(number: Int, color: Color, symbol: Symbol, shading: Shading, selected: Boolean):

  def toggleSelection: Card = copy(selected = !selected)

  override def toString: String =
    if selected then PrintUtil.cyan(s"$number$color$symbol$shading")
    else PrintUtil.yellow(s"$number$color$symbol$shading")

  def toStringEasy: String =
    if selected then PrintUtil.cyan(s"$number$color$symbol")
    else PrintUtil.yellow(s"$number$color$symbol")

  override def equals(obj: Any): Boolean = obj match {
    case other: Card =>
      number == other.number && color == other.color && symbol == other.symbol && shading == other.shading
    case _ => false
  }

  override def hashCode: Int =
    val prime = 31
    var result = 1
    result = prime * result + number
    result = prime * result + (if (color != null) color.hashCode else 0)
    result = prime * result + (if (symbol != null) symbol.hashCode else 0)
    result = prime * result + (if (shading != null) shading.hashCode else 0)
    result

enum Color:
  case RED, GREEN, BLUE
  override def toString: String =
    this match
      case RED => "R"
      case GREEN => "G"
      case BLUE => "B"

enum Symbol:
  case OVAL, SQUIGGLE, DIAMOND
  override def toString: String =
    this match
      case OVAL => "O"
      case SQUIGGLE => "S"
      case DIAMOND => "D"

enum Shading:
  case SOLID, OUTLINED, STRIPED
  override def toString: String =
    this match
      case SOLID => "F" // filled
      case OUTLINED => "L" // line
      case STRIPED => "C" // checkered