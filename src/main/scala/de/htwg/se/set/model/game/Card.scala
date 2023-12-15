package de.htwg.se.set.model.game

import de.htwg.se.set.model.{Color, ICard, Shading, Symbol}
import de.htwg.se.set.util.PrintUtil

sealed trait Card(val number: Int, val color: Color, val symbol: Symbol, val shading: Shading, val selected: Boolean)
  extends ICard:

  override def select: ICard

  override def unselect: ICard

  override def equals(obj: Any): Boolean = obj match
    case other: ICard =>
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