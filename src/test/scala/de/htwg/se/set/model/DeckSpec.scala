package de.htwg.se.set.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DeckSpec extends AnyWordSpec with Matchers:

  "A Deck" when:
    "constructed with easy mode" should:
      val easyDeck = new Deck(easy = true)
      "have all cards with SOLID shading" in:
        easyDeck.allCards.forall(_.shading == Shading.SOLID) shouldBe true
      "have a shuffled list of cards" in:
        val testDeck = new Deck(easy = true)
        easyDeck.allCards should not equal testDeck.allCards
    "constructed with normal mode" should:
      val normalDeck = new Deck(easy = false)
      "have cards with all types of shading" in:
        Shading.values.forall(shading => normalDeck.allCards.exists(_.shading == shading)) shouldBe true
      "have a shuffled list of cards" in:
        val testDeck = new Deck(easy = false)
        normalDeck.allCards should not equal testDeck.allCards
    "dealing cards to the table" should:
      val deck = new Deck(easy = true)
      val table = List.empty[Card]
      val number = 12
      val newTable = deck.tableCards(number, table, List[Card]())
      "add the right number of cards to the table" in:
        newTable.length shouldBe number
    "selecting cards" should:
      val deck = new Deck(easy = true)
      val table = deck.allCards.take(12)
      val card1 = table.head
      val card2 = table(1)
      val card3 = table(2)
      val newTable = deck.selectCards(table, card1, card2, card3)
      "toggle the selection of the selected cards" in:
        newTable.count(_.selected) shouldBe 3
      "leave other cards unselected" in:
        newTable.drop(3).forall(_.selected == false) shouldBe true
    "accessing a card by coordinate" should:
      val deck = new Deck(easy = true)
      val table = deck.allCards.take(12)
      val columns = 3
      val coordinate = "B2"
      val card = deck.cardAtCoordinate(table, coordinate, columns)
      "return the correct card" in:
        card shouldBe table(4)