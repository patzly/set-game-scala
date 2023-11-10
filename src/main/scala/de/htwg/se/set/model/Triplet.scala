package de.htwg.se.set.model

case class Triplet(card1: Card, card2: Card, card3: Card):
  
  if card1 == card2 || card1 == card3 || card2 == card3 then
    throw new IllegalArgumentException("Cards must be different")

  def isSet: Boolean =
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

  override def toString: String = card1.toString + "+" + card2.toString + "+" + card3.toString

  def toStringEasy: String = card1.toStringEasy + "+" + card2.toStringEasy + "+" + card3.toStringEasy

  private def cardsSet: Set[Card] = Set(card1, card2, card3)

  override def equals(obj: Any): Boolean = obj match
    case other: Triplet => cardsSet == other.cardsSet
    case _ => false

  override def hashCode: Int = cardsSet.hashCode