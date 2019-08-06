import org.scalatest._


class GitHubTest extends BaseTest {


  "Github" should {
    "return user infos" in {

      val res1 = await(githubDAO.getUserInfos("Kajy"))
      assert(res1.login == "Kajy")
    }
  }
}
