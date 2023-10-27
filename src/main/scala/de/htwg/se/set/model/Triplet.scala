package de.htwg.se.set.model

class Triplet(val card1: Card, val card2: Card, val card3: Card):
  
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
    val shadingDifferent = card1.shading != card2.shading && card2.shading != card3.shading
      && card1.shading != card3.shading
    val shadingSet = shadingEqual || shadingDifferent
    
    numberSet && colorSet && symbolSet && shadingSet