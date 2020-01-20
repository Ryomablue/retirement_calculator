package retcalc

import scala.annotation.tailrec

object RetCalc {
  def simulatePlan(interestRate: Double, nbOfMonthsSaving: Int, nbOfMonthsInRetirement: Int, netIncome: Int, currentExpenses: Int, initialCapital: Double): (Double, Double) = {
    val capitalAtRetirement = futureCapital(interestRate=interestRate, nbOfMonths = nbOfMonthsSaving, netIncome = netIncome, currentExpenses = currentExpenses, initialCapital = initialCapital)
    val capitalAfterDeath = futureCapital(interestRate= interestRate, nbOfMonths =  nbOfMonthsInRetirement, netIncome = 0, currentExpenses = currentExpenses, initialCapital = capitalAtRetirement)

    (capitalAtRetirement, capitalAfterDeath)
  }

  def futureCapital(interestRate: Double, nbOfMonths: Int, netIncome: Int,currentExpenses: Int, initialCapital: Double): Double = {
    val monthlySavings = netIncome - currentExpenses
    (0 until nbOfMonths).foldLeft(initialCapital)((accumulated, _) => accumulated * (1 + interestRate) + monthlySavings)

    def nextCapital(accumulated: Double, month: Int): Double = accumulated * (1 + interestRate) + monthlySavings
    (0 until nbOfMonths).foldLeft(initialCapital)(nextCapital)
  }

  def nbOfMonthsSaving(interestRate: Double, nbOfMonthsInRetirement: Int, netIncome: Int, currentExpenses: Int, initialCapital: Double): Int = {
    @tailrec
    def loop(months: Int): Int = {
      val (capitalAtRetirement, capitalAfterDeath) = simulatePlan(
        interestRate = interestRate, nbOfMonthsSaving = months, nbOfMonthsInRetirement = nbOfMonthsInRetirement,
        netIncome = netIncome, currentExpenses = currentExpenses, initialCapital = initialCapital)

      if (capitalAfterDeath > 0.0)
        months
      else
        loop(months + 1)
    }
    if (netIncome > currentExpenses)
      loop(0)
    else
      Int.MaxValue
  }
  sealed trait Returns
  case class FixedReturns(annualRate: Double) extends Returns
  case class VariableReturns(returns: Vector[VariableReturn]) extends Returns {
    def fromUntil(monthIdFrom: String, monthIdUntil: String):
    VariableReturns = VariableReturns(
      returns
        .dropWhile(_.monthId != monthIdFrom)
        .takeWhile(_.monthId != monthIdUntil)
    )
  }
  case class VariableReturn(monthId: String, monthlyRate: Double)
}
