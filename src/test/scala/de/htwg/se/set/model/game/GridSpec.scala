package de.htwg.se.set.model.game

import de.htwg.se.set.model
import de.htwg.se.set.model.game.base.{Card, Grid}
import de.htwg.se.set.model.{Color, Shading, Symbol}
import de.htwg.se.set.util.PrintUtil
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GridSpec extends AnyWordSpec with Matchers:
  
  "A Grid" when:
    "constructed in normal mode" should:
      "have a correct String representation" in:
        val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID)
        val card2 = Card(2, Color.GREEN, Symbol.SQUIGGLE, Shading.OUTLINED)
        val cards = List(card1, card2, card1, card2, card1, card2)
        val grid = Grid(2, cards, false)
        grid.toString should include(PrintUtil.yellow("1ROF"))
        grid.toString should include(PrintUtil.yellow("2GSL"))
        grid.toString should include("1")
        grid.toString should include("2")
        grid.toString should include("A")
        grid.toString should include("B")
    "constructed with a grid size unequal to the amount of cards" should:
      "throw an IllegalArgumentException" in:
        val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID)
        val card2 = Card(2, Color.GREEN, Symbol.SQUIGGLE, Shading.OUTLINED)
        val card3 = Card(3, Color.BLUE, Symbol.DIAMOND, Shading.STRIPED)
        val cards = List(card1, card2, card3, card1, card2, card3)
        val exception = intercept[IllegalArgumentException] {
          Grid(3, cards, true)
        }
        exception.getMessage shouldBe "Amount of cards has to be equal to the grid size"
    "constructed with different numbers of columns" should:
      "have a correct String representation for various grid sizes" in:
        for (columns <- 1 to 4)
          val cards = List.fill(columns * 3)(Card(1, Color.RED, Symbol.OVAL, Shading.SOLID))
          val grid = Grid(columns, cards, false)
          (65 until 65 + columns).foreach: charCode =>
            grid.toString should include(charCode.toChar.toString)
          (1 to 3).foreach: number =>
            grid.toString should include(number.toString)