package de.htwg.se.set.util

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PrintUtilSpec extends AnyWordSpec with Matchers:

  "The PrintUtil" should:
    "make text bold" in:
      PrintUtil.bold("bold") shouldBe "\u001B[1mbold\u001B[0m"
    "underline text" in:
      PrintUtil.underline("underline") shouldBe "\u001B[4munderline\u001B[0m"
    "make text red" in:
      PrintUtil.red("red") shouldBe "\u001B[31mred\u001B[0m"
    "make text green" in:
      PrintUtil.green("green") shouldBe "\u001B[32mgreen\u001B[0m"
    "make text yellow" in:
      PrintUtil.yellow("yellow") shouldBe "\u001B[33myellow\u001B[0m"
    "make text blue" in:
      PrintUtil.blue("blue") shouldBe "\u001B[34mblue\u001B[0m"
    "make text purple" in:
      PrintUtil.purple("purple") shouldBe "\u001B[35mpurple\u001B[0m"
    "make text cyan" in:
      PrintUtil.cyan("cyan") shouldBe "\u001B[36mcyan\u001B[0m"