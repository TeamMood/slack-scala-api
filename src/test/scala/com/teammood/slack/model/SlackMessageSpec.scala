package com.teammood.slack.model

import org.specs2.mutable.Specification
import play.api.libs.json.{Json, Writes}

class SlackMessageSpec extends Specification {

  "SlackMessage" should {

    "create the correct Json payload for a short Slack message" in {

      val sanitize: String => String = _.replaceAll("\n", "").replaceAll("\t", "").replaceAll(" ", "")

      val expected =
        """
          |{
          |  "blocks": [
          |    {
          |      "text": {
          |        "type": "mrkdwn",
          |        "text": "Hey Nicolas, how's your day today?"
          |      },
          |      "type": "section"
          |    }
          |  ]
          |}
          |""".stripMargin


      val title = SlackSection(Some(SlackText("Hey Nicolas, how's your day today?")))

      val message = SlackMessage(Seq(title))

      implicit val messageWriter: Writes[SlackMessage] = SlackMessage.slackSlackMessageWrites
      val json = Json.toJson(message)

      sanitize(json.toString()) must beEqualTo(sanitize(expected))

    }
  }
}
