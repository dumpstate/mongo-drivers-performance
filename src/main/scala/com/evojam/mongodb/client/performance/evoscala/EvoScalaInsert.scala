package com.evojam.mongodb.client.performance.evoscala

import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import com.evojam.mongodb.client._
import org.scalameter.api._

object EvoScalaInsert extends PerformanceTest.Quickbenchmark with EvoScalaCollection {
  performance of "mongo-driver-scala" in {
    val collection = getCollection()

    measure method "insertAll" in {
      using(documents) in { ds =>
        val insert = Future.sequence(ds.map(collection.insert(_)))
        Await.result(insert, Duration.Inf)

        Await.result(collection.drop(), Duration.Inf)
      }
    }
  }
}
