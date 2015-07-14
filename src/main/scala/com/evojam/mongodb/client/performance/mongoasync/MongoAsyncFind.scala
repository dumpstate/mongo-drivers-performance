package com.evojam.mongodb.client.performance.mongoasync

import scala.collection.JavaConversions._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Promise, Future}
import scala.concurrent.ExecutionContext.Implicits.global

import com.mongodb.async.{AsyncBatchCursor, SingleResultCallback}
import org.bson.Document
import org.scalameter.api._

object MongoAsyncFind extends PerformanceTest.Quickbenchmark with MongoAsyncCollection {
  performance of "mongodb-driver-async" in {
    val collection = getCollection()
    val ds = documents(InsertBeforeFind)
    insertBefore(collection, ds)

    def find(query: Document): Future[List[Document]] = {
      val res: Promise[List[Document]] = Promise()
      collection.find(query)
        .batchCursor(new SingleResultCallback[AsyncBatchCursor[Document]] {
        override def onResult(t: AsyncBatchCursor[Document], throwable: Throwable){
          if (throwable == null) {
            t.next(new SingleResultCallback[java.util.List[Document]] {
              override def onResult(t: java.util.List[Document], throwable: Throwable) {
                if (throwable != null) {
                  res.success(t.toList)
                  ()
                } else {
                  res.failure(throwable)
                  ()
                }
              }
            })
          } else {
            res.failure(throwable)
            ()
          }
        }
      })
      res.future
    }

    measure method "find" in {
      using(sizes) in { s =>
        val res = for (size <- 0 to s)
          yield find(new Document("field", "value"))
        Await.ready(Future.sequence(res), Duration.Inf)
      }
    }

    dropAfter(collection)
  }
}
