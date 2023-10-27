package de.htwg.se.set.util

object PrintUtil:

  private object ANSI:
    val BOLD = "\u001B[1m"
    val UNDERLINE = "\u001B[4m"
    val RED = "\u001B[31m"
    val GREEN = "\u001B[32m"
    val YELLOW = "\u001B[33m"
    val BLUE = "\u001B[34m"
    val PURPLE = "\u001B[35m"
    val CYAN = "\u001B[36m"
    val RESET = "\u001B[0m"

  def bold(text: String): String = ANSI.BOLD + text + ANSI.RESET

  def underline(text: String): String = ANSI.UNDERLINE + text + ANSI.RESET

  def red(text: String): String = ANSI.RED + text + ANSI.RESET

  def green(text: String): String = ANSI.GREEN + text + ANSI.RESET

  def yellow(text: String): String = ANSI.YELLOW + text + ANSI.RESET

  def blue(text: String): String = ANSI.BLUE + text + ANSI.RESET

  def purple(text: String): String = ANSI.PURPLE + text + ANSI.RESET

  def cyan(text: String): String = ANSI.CYAN + text + ANSI.RESET