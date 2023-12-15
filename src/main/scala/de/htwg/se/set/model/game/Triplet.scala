package de.htwg.se.set.model.game

import de.htwg.se.set.model.{ICard, ITriplet}

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

  override def toString: String = card1.toString + "+" + card2.toString + "+" + card3.toString

  override def equals(obj: Any): Boolean = obj match
    case other: ITriplet => Set(card1, card2, card3) == Set(other.card1, other.card2, other.card3)
    case _ => false

  override def hashCode: Int = Set(card1, card2, card3).hashCode