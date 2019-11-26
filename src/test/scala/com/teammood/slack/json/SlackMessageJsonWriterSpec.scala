package com.teammood.slack.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.teammood.slack.model.{SlackActions, SlackButtonElement, SlackMessage, SlackPlainText, SlackSection, SlackText}
import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification
import play.api.libs.json.{Json, Writes}

class SlackMessageJsonWriterSpec extends Specification {

  "SlackMessageJsonWriter" should {

    def assetEquals(actual: String, expected: String): MatchResult[Boolean] = {
      // Using Jackson to compare Json
      val mapper = new ObjectMapper()
      (mapper.readTree(actual) == mapper.readTree(expected)) must beTrue
    }

    "create the correct Json payload for a short Slack message" in {

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

      implicit val messageWriter: Writes[SlackMessage] = SlackMessageJsonWriter.slackSlackMessageWrites
      val json = Json.toJson(message)

      assetEquals(json.toString(), expected)
    }

    "create the correct Json payload for a Slack message with actions" in {

      val expected =
        """
          |{
          |  "blocks": [
          |   {
          |			"type": "actions",
          |     "block_id": "this is a block id",
          |			"elements": [
          |				{
          |					"type": "button",
          |					"text": {
          |						"type": "plain_text",
          |						"text": "Excellent"
          |					},
          |					"value": "excellent"
          |				}
          |			]
          |   }
          |   ]
          |}
          |""".stripMargin

      val buttons = SlackActions(
        Some("this is a block id"),
        Seq(
          SlackButtonElement(SlackPlainText("Excellent"), Some("excellent"))
        )
      )

      val message = SlackMessage(Seq(buttons))

      implicit val messageWriter: Writes[SlackMessage] = SlackMessageJsonWriter.slackSlackMessageWrites
      val json = Json.toJson(message)

      assetEquals(json.toString(), expected)
    }
  }
}
