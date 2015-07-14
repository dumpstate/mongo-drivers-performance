package com.evojam.mongodb.client.performance.mongocore

import scala.collection.JavaConversions._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

import org.scalameter.api._

object MongoCoreInsert extends PerformanceTest.Quickbenchmark with MongoCoreCollection {
  performance of "mongodb-driver" in {
    val collection = getCollection()

    measure method "insertAll" in {
      using(genDocuments) in { ds =>
        val insert = Future {
          collection.insertMany(ds)
        }
        Await.ready(insert, Duration.Inf)

        val drop = Future {
          collection.drop()
        }
        Await.ready(drop, Duration.Inf)
      }
    }
  }
}
