package de.htwg.se.set.model

import de.htwg.se.set.util.PrintUtil

sealed trait Card(val number: Int, val color: Color, val symbol: Symbol, val shading: Shading, val selected: Boolean):

  def select: Card

  def unselect: Card

  override def equals(obj: Any): Boolean = obj match
    case other: Card =>
      number == other.number && color == other.color && symbol == other.symbol && shading == other.shading
    case _ => false

  override def hashCode: Int =
    val prime = 31
    var result = 1
    result = prime * result + number
    result = prime * result + color.hashCode
    result = prime * result + symbol.hashCode
    result = prime * result + shading.hashCode
    result

object Card:

  private class NormalCard(number: Int, color: Color, symbol: Symbol, shading: Shading, selected: Boolean)
    extends Card(number, color, symbol, shading, selected):

    override def select: NormalCard = new NormalCard(number, color, symbol, shading, true)

    override def unselect: NormalCard = new NormalCard(number, color, symbol, shading, false)

    override def toString: String =
      val string = s"$number$color$symbol$shading"
      if selected then PrintUtil.cyan(string) else PrintUtil.yellow(string)

  private class EasyCard(number: Int, color: Color, symbol: Symbol, selected: Boolean)
    extends Card(number, color, symbol, Shading.SOLID, selected):

    override def select: EasyCard = new EasyCard(number, color, symbol, true)

    override def unselect: EasyCard = new EasyCard(number, color, symbol, false)

    override def toString: String =
      val string = s"$number$color$symbol"
      if selected then PrintUtil.cyan(string) else PrintUtil.yellow(string)

  def apply(number: Int, color: Color, symbol: Symbol, shading: Shading): Card =
    NormalCard(number, color, symbol, shading, false)

  def apply(number: Int, color: Color, symbol: Symbol): Card =
    EasyCard(number, color, symbol, false)

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