package com.evojam.mongodb.client.performance

import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import com.evojam.mongodb.client._
import com.evojam.mongodb.client.performance.util.DocumentGenerator
import org.scalameter.api._
import reactivemongo.api.MongoDriver

object PerfTest extends PerformanceTest.Quickbenchmark with DocumentGenerator {
  val DbName = "performancetest"
  val ColName = "collection"

  val sizes: Gen[Int] = Gen.range("sizes")(100, 1000, 100)

  val schema = nextSchema(size = nextDocSize())

  println(s"Schema: $schema")

  val docs = sizes
    .map(size => documents(Some(size), schema))
    .map(doc => (doc, docs2ReactiveMongoDocument(doc, schema)))
    .map(tpl => tpl._1.zip(tpl._2))

  performance of "mongo-driver-scala" in {
    measure method "insertAll" in {
      val collection = MongoClients.create()
        .database(DbName)
        .collection(ColName)

      val documents = docs.map(_.map(_._1))

      using(docs.map(_.map(_._1))) in { ds =>
        val insert = collection
          .insertAll(ds)
          .flatMap(_ => collection.count())
        Await.result(insert, Duration.Inf)

        Await.result(collection.drop(), Duration.Inf)
      }
    }
  }

  performance of "reactivemongo" in {
    measure method "insertAll" in {
      val driver = new MongoDriver()
      val connection = driver.connection(List("localhost"))
      val db = connection(DbName)
      val collection = db(ColName)

      using(docs.map(_.map(_._2))) in { ds =>
        val insert =
          Future.sequence(ds.map(d => collection.insert(d)))
        Await.result(insert, Duration.Inf)

        Await.result(collection.drop(), Duration.Inf)
      }
    }
  }
}
