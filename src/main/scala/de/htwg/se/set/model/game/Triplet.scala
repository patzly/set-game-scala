package de.htwg.se.set.model.game

import de.htwg.se.set.model.{ICard, ITriplet}
import play.api.libs.json.{JsValue, Json, Reads, Writes}

import scala.xml.{Elem, Node}

case class Triplet(card1: ICard, card2: ICard, card3: ICard) extends ITriplet:
  
  if card1 == card2 || card1 == card3 || card2 == card3 then
    throw new IllegalArgumentException("Cards must be different")

  override def isSet: Boolean =
    val numberEqual = card1.number == card2.number && card2.number == card3.number
    val numberDifferent = card1.number != card2.number && card2.number != card3.number && card1.number != card3.number
    val numberSet = numberEqual || numberDifferent
    val colorEqual = card1.color == card2.color && card2.color == card3.color
    val colorDifferent = card1.color != card2.color && card2.color != card3.color && card1.color != card3.color
    val colorSet = colorEqual || colorDifferent
    val symbolEqual = card1.symbol == card2.symbol && card2.symbol == card3.symbol
    val symbolDifferent = card1.symbol != card2.symbol && card2.symbol != card3.symbol && card1.symbol != card3.symbol
    val symbolSet = symbolEqual || symbolDifferent
    val shadingEqual = card1.shading == card2.shading && card2.shading == card3.shading
    val shadingDifferent = card1.shading != card2.shading && card2.shading != card3.shading && card1.shading != card3.shading
    val shadingSet = shadingEqual || shadingDifferent
    numberSet && colorSet && symbolSet && shadingSet

  override def toXml: Elem =
    <triplet>
      <card1>{card1.toXml}</card1>
      <card2>{card2.toXml}</card2>
      <card3>{card3.toXml}</card3>
    </triplet>

  override def toJson: JsValue = Json.obj(
    "card1" -> card1.toJson,
    "card2" -> card2.toJson,
    "card3" -> card3.toJson
  )

  override def toString: String = card1.toString + "+" + card2.toString + "+" + card3.toString

  override def equals(obj: Any): Boolean = obj match
    case other: ITriplet => Set(card1, card2, card3) == Set(other.card1, other.card2, other.card3)
    case _ => false

  override def hashCode: Int = Set(card1, card2, card3).hashCode

object Triplet:
  
  def fromXml(node: Node): ITriplet =
    val card1 = Card.fromXml((node \ "card1").head)
    val card2 = Card.fromXml((node \ "card2").head)
    val card3 = Card.fromXml((node \ "card3").head)
    Triplet(card1, card2, card3)

  def fromJson(json: JsValue): ITriplet =
    val card1 = Card.fromJson((json \ "card1").get)
    val card2 = Card.fromJson((json \ "card2").get)
    val card3 = Card.fromJson((json \ "card3").get)
    Triplet(card1, card2, card3)

  implicit val writes: Writes[ITriplet] = (triplet: ITriplet) => triplet.toJson