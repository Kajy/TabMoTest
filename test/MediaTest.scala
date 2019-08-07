import play.api.libs.json.{JsString, Json}

class MediaTest extends BaseTest {


  "Media service" should {
    "return ok" in {

      val res1 = await(mediaService.putMedia(Some(Json.parse(
        """
          |{
          |	"title": "test",
          |	"country": "FRA",
          |	"year": 2005,
          |	"french_release" : "2006/12/31",
          |	"genre": ["Nul", "Classique"],
          |	"ranking": 1
          |}
        """.stripMargin))))
      assert(res1.isRight)
    }
    "return ko title" in {
      val res = await(mediaService.putMedia(Some(Json.parse(
        """
          |{
          |	"title": "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest",
          |	"country": "FRA",
          |	"year": 2005,
          |	"french_release" : "2006/12/31",
          |	"genre": ["Nul", "Classique"],
          |	"ranking": 1
          |}
        """.stripMargin))))
      assert(res.isLeft)
      assert(res.left.get == JsString("requirement failed: Media title length must be < 250"))
    }
    "return ko country" in {
      val res = await(mediaService.putMedia(Some(Json.parse(
        """
          |{
          |	"title": "test",
          |	"country": "FRAA",
          |	"year": 2005,
          |	"french_release" : "2006/12/31",
          |	"genre": ["Nul", "Classique"],
          |	"ranking": 1
          |}
        """.stripMargin))))
      assert(res.isLeft)
      assert(res.left.get == JsString("requirement failed: Media country format require 3 letters (ex: [FRA], [GER], [SPN] ..."))

      val res2 = await(mediaService.putMedia(Some(Json.parse(
        """
          |{
          |	"title": "test",
          |	"country": "FR1",
          |	"year": 2005,
          |	"french_release" : "2006/12/31",
          |	"genre": ["Nul", "Classique"],
          |	"ranking": 1
          |}
        """.stripMargin))))
      assert(res2.isLeft)
      assert(res2.left.get == JsString("requirement failed: Media country format require 3 letters (ex: [FRA], [GER], [SPN] ..."))

      val res3 = await(mediaService.putMedia(Some(Json.parse(
        """
          |{
          |	"title": "test",
          |	"country": "fra",
          |	"year": 2005,
          |	"french_release" : "2006/12/31",
          |	"genre": ["Nul", "Classique"],
          |	"ranking": 1
          |}
        """.stripMargin))))
      assert(res3.isLeft)
      assert(res3.left.get == JsString("requirement failed: Media country format require 3 letters (ex: [FRA], [GER], [SPN] ..."))
    }
    "return ko release date" in {

      val res = await(mediaService.putMedia(Some(Json.parse(
        """
          |{
          |	"title": "test",
          |	"country": "FRA",
          |	"year": 2005,
          |	"french_release" : "2006/12/32x",
          |	"genre": ["Nul", "Classique"],
          |	"ranking": 1
          |}
        """.stripMargin))))
      assert(res.isLeft)
      assert(res.left.get == JsString("requirement failed: Media date format require (YYYY/MM/DD)"))
    }
    "return ko genre too long" in {

      val res = await(mediaService.putMedia(Some(Json.parse(
        """
          |{
          |	"title": "test",
          |	"country": "FRA",
          |	"year": 2005,
          |	"french_release" : "2006/12/31",
          |	"genre": ["Nul", "ClassiqueClassiqueClassiqueClassiqueClassiqueClassiqueClassiqueClassiqueClassiqueClassiqueClassiqueClassique"],
          |	"ranking": 1
          |}
        """.stripMargin))))
      assert(res.isLeft)
      assert(res.left.get == JsString("requirement failed: Media genre length must be < 50"))
    }
    "return ko bad ranking" in {

      val res = await(mediaService.putMedia(Some(Json.parse(
        """
          |{
          |	"title": "test",
          |	"country": "FRA",
          |	"year": 2005,
          |	"french_release" : "2006/12/31",
          |	"genre": ["Nul", "Classique"],
          |	"ranking": 999
          |}
        """.stripMargin))))
      assert(res.isLeft)
      assert(res.left.get == JsString("requirement failed: Media ranking must be between 0 and 10"))
    }
  }
}
