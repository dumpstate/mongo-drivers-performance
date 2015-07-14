package com.evojam.mongodb.client.performance.evoscala

import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import com.evojam.mongodb.client._
import org.bson.Document
import org.scalameter.api._

object EvoScalaFind extends PerformanceTest.Quickbenchmark with EvoScalaCollection {
  performance of "mongo-driver-scala" in {
    val collection = getCollection()
    val ds = documents(InsertBeforeFind)
    insertBefore(collection, ds)

    measure method "find" in {
      using(sizes) in { s =>
        val res = for (size <- 0 to s)
          yield collection.find(new Document("field", "value")).collect[Document]
        Await.ready(Future.sequence(res), Duration.Inf)
      }
    }

    dropAfter(collection)
  }
}

