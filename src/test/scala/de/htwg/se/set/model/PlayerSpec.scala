package de.htwg.se.set.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PlayerSpec extends AnyWordSpec with Matchers:

  "A Player" when:
    val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, selected = false)
    val card2 = Card(2, Color.RED, Symbol.OVAL, Shading.SOLID, selected = false)
    val card3 = Card(3, Color.RED, Symbol.OVAL, Shading.SOLID, selected = false)
    val set = Triplet(card1, card2, card3)

    "new" should:
      "have a correct index" in:
        val player = Player(1, singlePlayer = true, easy = true, List())
        player.index shouldBe 0

    "found a set" should:
      val nonSetCard1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, selected = false)
      val nonSetCard2 = Card(1, Color.GREEN, Symbol.OVAL, Shading.SOLID, selected = false)
      val nonSetCard3 = Card(3, Color.RED, Symbol.OVAL, Shading.SOLID, selected = false)
      val nonSet = Triplet(nonSetCard1, nonSetCard2, nonSetCard3)
      "add the set to the player's sets if it's not already found" in:
        val player = Player(1, singlePlayer = true, easy = true, List())
        val updatedPlayer = player.foundSet(set)
        updatedPlayer.sets should contain(set)
      "not add the set if it's already found" in:
        val player = Player(1, singlePlayer = true, easy = true, List(set))
        val updatedPlayer = player.foundSet(set)
        updatedPlayer.sets.length shouldBe 1
      "remove a set from the player's sets if the found set is not a set and player has at least one set" in:
        val player = Player(1, singlePlayer = false, easy = true, List(set))
        val updatedPlayer = player.foundSet(nonSet)
        updatedPlayer.sets shouldNot contain(set)
      "not remove a set if the player has no sets" in:
        val player = Player(1, singlePlayer = false, easy = true, List())
        val updatedPlayer = player.foundSet(nonSet)
        updatedPlayer.sets shouldBe empty

    "printing" should:
      "display the correct string for single players" in:
        val player = Player(2, singlePlayer = true, easy = true, List(set))
        player.toString should include("1 of 3 SETs found:")
      "display the correct string in multiplayer mode" in:
        val player = Player(2, singlePlayer = false, easy = true, List(set))
        player.toString should include("1")