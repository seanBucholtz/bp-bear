package app.bpbear.util

object Validation {

  def validateSystolic(value: Int): Boolean = value >= 70 && value <= 250

  def validateDiastolic(value: Int): Boolean = value >= 40 && value <= 150

  def validatePulse(value: Int): Boolean = value >= 30 && value <= 200

  def validateUserEmail(email: String): Boolean =
    email.contains("@") && email.length >= 5

}
