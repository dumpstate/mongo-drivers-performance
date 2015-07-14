package com.evojam.mongodb.client.performance.reactivemongo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

import org.scalameter.api._

object ReactiveMongoInsert extends PerformanceTest.Quickbenchmark with ReactiveMongoCollection {
  performance of "reactivemongo" in {
    val collection = getCollection()

    measure method "insertAll" in {
      using(allDocuments.map(_.map(_._2))) in { ds =>
        val insert = Future.sequence(ds.map(collection.insert(_)))
        Await.result(insert, Duration.Inf)

        Await.result(collection.drop(), Duration.Inf)
      }
    }
  }
}
