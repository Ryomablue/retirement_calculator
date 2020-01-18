package retcalc

object RetCalc {
  def simulatePlan(interestRate: Double, nbOfMonthsSavings: Int, nbOfMonthsInRetirement: Int, netIncome: Int, currentExpenses: Int, initialCapital: Double): (Double, Double) = ???

  def futureCapital(interestRate: Double, nbOfMonths: Int, netIncome: Int,currentExpenses: Int, initialCapital: Double): Double = {
    val monthlySavings = netIncome - currentExpenses
    (0 until nbOfMonths).foldLeft(initialCapital)((accumulated, _) => accumulated * (1 + interestRate) + monthlySavings)

    def nextCapital(accumulated: Double, month: Int): Double = accumulated * (1 + interestRate) + monthlySavings
    (0 until nbOfMonths).foldLeft(initialCapital)(nextCapital)
  }
}
