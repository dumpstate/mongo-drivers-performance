package com.evojam.mongodb.client.performance.mongoasync

import scala.collection.JavaConversions._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Promise}

import com.mongodb.async.SingleResultCallback
import org.scalameter.api._

object MongoAsyncInsert extends PerformanceTest.Quickbenchmark with MongoAsyncCollection {
  performance of "mongodb-driver-async" in {
    val collection = getCollection()

    measure method "insertMany" in {
      using(genDocuments) in { ds =>
        val insert: Promise[Void] = Promise()
        collection.insertMany(ds, new SingleResultCallback[Void] {
          override def onResult(t: Void, throwable: Throwable): Unit = {
            if (throwable == null) {
              insert.success(t)
              ()
            } else {
              insert.failure(throwable)
              ()
            }
          }
        })
        Await.result(insert.future, Duration.Inf)

        val drop: Promise[Void] = Promise()
        collection.drop(new SingleResultCallback[Void] {
          override def onResult(t: Void, throwable: Throwable): Unit = {
            if (throwable == null) {
              drop.success(t)
              ()
            } else {
              drop.failure(throwable)
              ()
            }
          }
        })
        Await.result(drop.future, Duration.Inf)
      }
    }
  }
}
