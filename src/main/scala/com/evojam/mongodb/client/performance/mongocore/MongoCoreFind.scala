package com.evojam.mongodb.client.performance.mongocore

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

import org.bson.Document
import org.scalameter.api.PerformanceTest

object MongoCoreFind extends PerformanceTest.Quickbenchmark with MongoCoreCollection {
  performance of "mongo-driver-async" in {
    val collection = getCollection()
    val ds = documents(InsertBeforeFind)
    insertBefore(collection, ds)

    def find(query: Document): Future[List[Document]] = Future {
      val cursor = collection.find(query)
        .iterator()
      val res = scala.collection.mutable.ArrayBuffer.empty[Document]
      while(cursor.hasNext) {
        res += cursor.next()
      }
      res.toList
    }

    measure method "find" in {
      using(sizes) in { s =>
        val res = for (size <- 0 to s)
          yield find(new Document("field", "value"))
        Await.result(Future.sequence(res), Duration.Inf)
      }
    }

    dropAfter(collection)
  }
}
