package pokebowl.model

import pokebowl.model.PlayResult._

/**
  * @author Mark Soule on 1/29/16.
  */
class KickOff extends Play {
  override def calculateOdds(team: Team): Array[PlayResult] = {
    Array.fill(100)(PlayResult.Average)
  }

  override def getDisplayText(): String = "kickoff"
}
