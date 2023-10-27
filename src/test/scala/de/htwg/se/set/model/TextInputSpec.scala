package de.htwg.se.set.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TextInputSpec extends AnyWordSpec with Matchers:

  "A TextInput" when:
    "constructed with an empty string" should:
      val textInput = new TextInput("")
      "report being empty" in:
        textInput.nonEmpty shouldBe false
      "report not having coordinates" in:
        textInput.hasCoordinates shouldBe false
      "return an empty list of coordinates" in:
        textInput.coordinates shouldBe List()
    "constructed with a non-empty string without coordinates" should:
      val textInput = new TextInput("hello world")
      "report not being empty" in:
        textInput.nonEmpty shouldBe true
      "report not having coordinates" in:
        textInput.hasCoordinates shouldBe false
      "return an empty list of coordinates" in:
        textInput.coordinates shouldBe List()
    "constructed with a valid coordinates string" should:
      val textInput = new TextInput("A1 B2 C3")
      "report having coordinates" in:
        textInput.hasCoordinates shouldBe true
      "return a list of coordinates" in:
        textInput.coordinates shouldBe List("A1", "B2", "C3")
    "constructed with a valid but messy coordinates string" should:
      val textInput = new TextInput(" A1  B2   C3   ")
      "report having coordinates" in :
        textInput.hasCoordinates shouldBe true
      "return a list of coordinates" in :
        textInput.coordinates shouldBe List("A1", "B2", "C3")