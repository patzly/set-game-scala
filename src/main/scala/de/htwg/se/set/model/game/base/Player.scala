package de.htwg.se.set.model.game.base

import de.htwg.se.set.model.{IPlayer, ITriplet}
import de.htwg.se.set.util.PrintUtil

import scala.xml.{Elem, Node}

case class Player(number: Int, singlePlayer: Boolean, easy: Boolean, sets: List[ITriplet]) extends IPlayer:

  override def index: Int = number - 1

  override def setSets(sets: List[ITriplet]): IPlayer = copy(sets = sets)

  override def toXml: Elem =
    <player>
      <number>{number}</number>
      <singlePlayer>{singlePlayer}</singlePlayer>
      <easy>{easy}</easy>
      <sets>{sets.map(set => set.toXml)}</sets>
    </player>

  override def toString: String =
    if singlePlayer then
      val max = if easy then 3 else 6
      val setStrings = sets.map(set => set.toString)
      sets.length + " of " + max + " SETs found:\n" + setStrings.mkString("\n")
    else
      "Player " + number + ": " + PrintUtil.purple(sets.length.toString)

object Player:
  
  def fromXml(node: Node): IPlayer =
    val number = (node \ "number").text.toInt
    val singlePlayer = (node \ "singlePlayer").text.toBoolean
    val easy = (node \ "easy").text.toBoolean
    val sets = (node \ "sets" \ "set").map(Triplet.fromXml).toList
    Player(number, singlePlayer, easy, sets)