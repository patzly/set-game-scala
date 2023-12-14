package de.htwg.se.set.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PlayerSpec extends AnyWordSpec with Matchers:

  "A Player" when:
    val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID)
    val card2 = Card(2, Color.RED, Symbol.OVAL, Shading.SOLID)
    val card3 = Card(3, Color.RED, Symbol.OVAL, Shading.SOLID)
    val set = Triplet(card1, card2, card3)

    "new" should:
      "have a correct index" in:
        val player = Player(1, singlePlayer = true, easy = true, List())
        player.index shouldBe 0

    "printing" should:
      "display the correct string for single-player in easy mode" in:
        val player = Player(1, singlePlayer = true, easy = true, List(set))
        player.toString should include("1 of 3 SETs found:")
      "display the correct string for single-player in normal mode" in:
        val player = Player(1, singlePlayer = true, easy = false, List(set))
        player.toString should include("1 of 6 SETs found:")
      "display the correct string for multiplayer in easy mode" in:
        val player = Player(2, singlePlayer = false, easy = true, List(set))
        player.toString should include("2")
      "display the correct string for multiplayer in normal mode" in:
        val player = Player(2, singlePlayer = false, easy = false, List(set))
        player.toString should include("2")