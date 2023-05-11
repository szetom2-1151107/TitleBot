package model

import java.util.concurrent.ConcurrentHashMap
import javax.inject.Singleton

object TitleResultCacheManager {

  private case class CacheKey(url: String)

  private val cache = new ConcurrentHashMap[CacheKey, TitleResult]

  private def assembleCacheKey(url: String): CacheKey = {
    CacheKey(url)
  }

  def put(url: String, result: TitleResult): Unit = {
    val key = assembleCacheKey(url)
    cache.put(key, result)
  }

  def get(url: String): Option[TitleResult] = {
    val key = assembleCacheKey(url)
    cache.containsKey(key) match {
      case false => None
      case true => Some(cache.get(key))
    }
  }

  def invalidate(url: String): Unit = {
    cache.keySet().forEach(key => {
      if (key.url == url) {
        cache.remove(key)
      }
    })
  }

  def getSize(): Int = {
    cache.size()
  }
}
