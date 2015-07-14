package com.evojam.mongodb.client.performance.reactivemongo

import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import org.scalameter.api._
import reactivemongo.bson.{BSONString, BSONDocument}

object ReactiveMongoFind extends PerformanceTest.Quickbenchmark with ReactiveMongoCollection {
  performance of "reactivemongo" in {
    val collection = getCollection()
    val ds = documents(InsertBeforeFind)
    insertBefore(collection, ds)

    measure method "find" in {
      using(sizes) in { s =>
        val res = for(size <- 0 to s)
          yield collection.find(BSONDocument(List(("field", BSONString("value")))))
            .cursor[BSONDocument]()
            .collect[List]()
        Await.ready(Future.sequence(res), Duration.Inf)
      }
    }

    dropAfter(collection)
  }
}
