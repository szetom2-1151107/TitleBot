package model

import org.scalatestplus.play.PlaySpec

class TitleResultCacheManagerTest extends PlaySpec {
  trait Fixture {
    TitleResultCacheManager.put("A", TitleResult("title", "favicon"))
  }

  "put should add the key-value pair into the cache" in new Fixture {
    TitleResultCacheManager.put("B", TitleResult("title", "favicon"))
    TitleResultCacheManager.getSize() mustBe 2
  }

  "get should retrieve successfully" in new Fixture {
    TitleResultCacheManager.get("A").isDefined mustBe true
  }

  "get should return None" in new Fixture {
    TitleResultCacheManager.get("C") mustBe None
  }

  "invalidate should remove item from cache" in new Fixture {
    TitleResultCacheManager.invalidate("A")
    TitleResultCacheManager.invalidate("B")
    TitleResultCacheManager.getSize() mustBe 0
  }

}
