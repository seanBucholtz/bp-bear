package app.bpbear.domain

import zio.json.*
import java.time.Instant

case class BloodPressureReading(
                                 id: Long,
                                 userId: Long,
                                 systolic: Int,
                                 diastolic: Int,
                                 pulse: Int,
                                 recordedAt: Instant
                               )

object BloodPressureReading {
  implicit val codec: JsonCodec[BloodPressureReading] =
    DeriveJsonCodec.gen[BloodPressureReading]
}
