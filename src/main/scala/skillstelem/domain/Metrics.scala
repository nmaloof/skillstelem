package skillstelem.domain

import io.circe.Codec

type Metric[T] = Map[String, T]

extension (m: Metric[Int]) {
   def increment(k: String): Metric[Int] = m.updated(k, m.getOrElse(k, 0) + 1)
}

case class Metrics() derives Codec
