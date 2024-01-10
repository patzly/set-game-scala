package de.htwg.se.set.model.game.base

import de.htwg.se.set.model.{Color, ICard, Shading, Symbol}
import de.htwg.se.set.util.PrintUtil

import scala.xml.{Elem, Node}

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

    override def toXml: Elem =
      <card>
        <number>{number}</number>
        <color>{color}</color>
        <symbol>{symbol}</symbol>
        <shading>{shading}</shading>
        <selected>{selected}</selected>
      </card>

    override def toString: String =
      val string = s"$number${color.short}${symbol.short}${shading.short}"
      if selected then PrintUtil.cyan(string) else PrintUtil.yellow(string)

  private class EasyCard(number: Int, color: Color, symbol: Symbol, selected: Boolean)
    extends Card(number, color, symbol, Shading.SOLID, selected):

    override def select: EasyCard = new EasyCard(number, color, symbol, true)

    override def unselect: EasyCard = new EasyCard(number, color, symbol, false)

    override def toXml: Elem =
      <card>
        <number>{number}</number>
        <color>{color}</color>
        <symbol>{symbol}</symbol>
        <selected>{selected}</selected>
      </card>

    override def toString: String =
      val string = s"$number${color.short}${symbol.short}"
      if selected then PrintUtil.cyan(string) else PrintUtil.yellow(string)

  def apply(number: Int, color: Color, symbol: Symbol, shading: Shading): Card =
    NormalCard(number, color, symbol, shading, false)

  def apply(number: Int, color: Color, symbol: Symbol): Card =
    EasyCard(number, color, symbol, false)

  def fromXml(node: Node): Card =
    val number = (node \ "number").text.toInt
    val color = Color.valueOf((node \ "color").text)
    val symbol = Symbol.valueOf((node \ "symbol").text)
    val selected = (node \ "selected").text.toBoolean
    (node \ "shading").headOption match
      case Some(shadingNode) =>
        val shading = Shading.valueOf(shadingNode.text)
        NormalCard(number, color, symbol, shading, selected)
      case None =>
        EasyCard(number, color, symbol, selected)