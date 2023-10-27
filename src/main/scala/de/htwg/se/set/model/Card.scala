package de.htwg.se.set.model

case class Card(number: Int, color: Color, symbol: Symbol, shading: Shading):

  override def toString: String = s"$number$color$symbol$shading"

  def toStringEasy: String = s"$number$color$symbol"

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