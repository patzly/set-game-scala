package de.htwg.se.set.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SettingsSpec extends AnyWordSpec with Matchers:

  "A Settings" when:
    "initialized with a valid player count and difficulty setting" should:
      "create a Settings object with the correct values" in:
        val playerCount = 2
        val easy = true
        val inGame = false
        val settings = Settings(playerCount, easy, inGame)
        settings.playerCount should be(playerCount)
        settings.singlePlayer should be(playerCount == 1)
        settings.easy should be(easy)
        settings.inGame should be(inGame)
    "initialized with a single player" should:
      "create a Settings object representing a single player" in:
        val settings = Settings(1, false)
        settings.toString should include("1 player")
        settings.singlePlayer should be(true)
    "initialized with multiple players" should:
      "create a Settings object representing multiple players" in:
        val settings = Settings(3, false)
        settings.toString should include("3 players")
        settings.singlePlayer should be(false)
    "initialized with easy mode" should:
      "create a Settings object in easy mode" in:
        val settings = Settings(1, true)
        settings.toString should include("easy mode")
      "be equal to another Settings object with the same player count and easy mode, regardless of in-game status" in:
        val settings1 = Settings(1, true)
        val settings2 = Settings(1, true, true)
        settings1 shouldEqual settings2
    "initialized with normal mode" should:
      "create a Settings object in normal mode" in:
        val settings = Settings(1, false)
        settings.toString should include("normal mode")
    "initialized with an invalid player count" should:
      "throw an IllegalArgumentException" in:
        an[IllegalArgumentException] should be thrownBy Settings(0, false)