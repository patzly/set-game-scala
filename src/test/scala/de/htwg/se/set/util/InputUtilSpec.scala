package de.htwg.se.set.util

import de.htwg.se.set.util.InputUtil.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class InputUtilSpec extends AnyWordSpec with Matchers:

  "InputUtil" when:
    "processing string inputs" should:
      "correctly handle undo, redo, finish, and text inputs" in:
        InputUtil.stringInput("u", true, false, false) shouldBe UndoInput
        InputUtil.stringInput("u", false, false, false) shouldBe a[InvalidInput]
        InputUtil.stringInput("r", false, true, false) shouldBe RedoInput
        InputUtil.stringInput("r", false, false, false) shouldBe a[InvalidInput]
        InputUtil.stringInput("f", false, false, true) shouldBe FinishInput
        InputUtil.stringInput("f", false, false, false) shouldBe TextInput("f")
        InputUtil.stringInput("test", false, false, false) shouldBe TextInput("test")

    "processing integer inputs" should:
      "handle valid and invalid integer strings" in:
        InputUtil.intInput("42", false, false) shouldBe NumberInput(42)
        InputUtil.intInput("invalid", false, false) shouldBe a[InvalidInput]
        InputUtil.intInput("u", true, false) shouldBe UndoInput
        InputUtil.intInput("r", false, true) shouldBe RedoInput
      "handle integer inputs within a specific range" in:
        InputUtil.intInput("5", 1, 10, false, false) shouldBe NumberInput(5)
        InputUtil.intInput("0", 1, 10, false, false) shouldBe a[InvalidInput]
        InputUtil.intInput("u", 1, 10, true, false) shouldBe UndoInput
        InputUtil.intInput("r", 1, 10, false, true) shouldBe RedoInput

    "processing coordinates input" should:
      "handle valid and invalid coordinates" in:
        val coordinates = List("A1", "B2", "C3")
        InputUtil.coordinatesInput("A1 B2 C3", false, false) shouldBe CoordinatesInput(coordinates)
        InputUtil.coordinatesInput("invalid", false, false) shouldBe a[InvalidInput]
        InputUtil.coordinatesInput("A1 A1 A1", false, false) shouldBe a[InvalidInput]
        InputUtil.coordinatesInput("u", true, false) shouldBe UndoInput
        InputUtil.coordinatesInput("r", false, true) shouldBe RedoInput

    "processing finish input" should:
      "handle finish and invalid inputs" in:
        InputUtil.finishInput("f", false, false) shouldBe FinishInput
        InputUtil.finishInput("invalid", false, false) shouldBe a[InvalidInput]
        InputUtil.finishInput("u", true, false) shouldBe UndoInput
        InputUtil.finishInput("r", false, true) shouldBe RedoInput