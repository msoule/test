package pokebowl.game

import pokebowl.controller.PlayMode
import pokebowl.controller.PlayMode._
import pokebowl.model.Team

/**
  * Tracks the state of the football game.
  *
  * @author Mark Soule on 1/27/16.
  */
class GameState(homeTeam: Team, awayTeam: Team) {
  var homeScore = 0
  var awayScore = 0
  var lineOfScrimmage: Int = 0
  var firstDownMarker: Int = 10
  var down: Int = 1
  var possession = awayTeam
  var playMode: PlayMode = PlayMode.SelectPlay
  var playCount = 0
  var currentQuarter = 1

  // constants
  private val MAX_YARDS = 40
  private val FIRST_DOWN_YARDS = 10
  private val PLAYS_PER_QUARTER = 10
  private val TOUCHDOWN_SCORE = 6
  private val EXTRA_POINT_SCORE = 1
  private val FIELD_GOAL_SCORE = 3


  def changeLineOfScrimmage(change: Int): Seq[String] = {
    var messages: Seq[String] = Seq()
    lineOfScrimmage += change
    if(lineOfScrimmage >= MAX_YARDS) {
      addScore(possession, TOUCHDOWN_SCORE)
      playMode = PlayMode.ExtraPoint
      down = 1
      messages = messages :+ "... TOUCHDOWN!"
    }
    else if(lineOfScrimmage >= firstDownMarker) {
      down = 1
      firstDownMarker = lineOfScrimmage + FIRST_DOWN_YARDS
      messages = messages :+ "First down!"
    }
    messages
  }

  def scoreExtraPoint(): Seq[String] = {
    addScore(possession, 1)
    playMode = PlayMode.KickOff
    Seq("Extra point is good")
  }

  def kickOff(fieldPosition: Int) = {
    changePossession()
    lineOfScrimmage = fieldPosition
    firstDownMarker = lineOfScrimmage + FIRST_DOWN_YARDS
    down = 1
    playMode = PlayMode.SelectPlay
    Seq(s"${possession.name} start possession at $getFieldPositionText")
  }

  def addScore(scoringTeam: Team, amount: Int) = {
    if(scoringTeam == homeTeam) homeScore += amount else awayScore += amount
  }

  def changePossession(): Unit = {
    if(possession == awayTeam) possession = homeTeam else possession = awayTeam
  }

  def advanceGameClock(): Seq[String] = {
    playCount += 1
    var messages = Seq(s"Play #$playCount")
    if(playCount >= PLAYS_PER_QUARTER) {
      messages = messages :+ s"Quarter $currentQuarter is over"
      currentQuarter += 1
      currentQuarter match {
        case 3 => messages = messages ++ Seq(s"Halftime!", s"Quarter $currentQuarter begins")
        case 5 => messages = messages :+ s"Game over!"
        case _ => messages = messages :+ s"Quarter $currentQuarter begins!"
      }
      playCount = 0
    }
    messages
  }

  def getFieldPositionText: String = {
    val intermediate = lineOfScrimmage - (MAX_YARDS / 2)
    var our = true
    if(intermediate > 0)
      our = false
    val yardLine = (MAX_YARDS / 2) - Math.abs(intermediate)
    val yardString = s"$yardLine yard line"
    if(yardLine == (MAX_YARDS / 2))
      yardString
    else if(our)
      s"their $yardString"
    else
      s"opponent's $yardString"
  }

  def getNonPossesingTeam = {
    if(possession == awayTeam) homeTeam else awayTeam
  }

  def getHomeTeam = {
    homeTeam
  }

  def getAwayTeam = {
    awayTeam
  }
}
