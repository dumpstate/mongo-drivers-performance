package com.evojam.mongodb.client.performance

import org.scalameter.api.Gen

trait Collection[T, R] {
  val documents: Gen[List[T]]

  def getCollection(): R
}
