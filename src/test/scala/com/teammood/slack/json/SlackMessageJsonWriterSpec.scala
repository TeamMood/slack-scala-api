package com.teammood.slack.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.teammood.slack.model.{SlackActions, SlackButtonElement, SlackContext, SlackImageBlock, SlackMessage, SlackPlainText, SlackSection, SlackText}
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
        "this is a block id",
        Seq(
          SlackButtonElement(SlackPlainText("Excellent"), value = Some("excellent"))
        )
      )

      val message = SlackMessage(Seq(buttons))

      implicit val messageWriter: Writes[SlackMessage] = SlackMessageJsonWriter.slackSlackMessageWrites
      val json = Json.toJson(message)

      assetEquals(json.toString(), expected)
    }

    "create the correct Json payload for a Slack context" in {

      val expected =
        """
          |{
          |  "blocks": [
          |    {
          |      "type": "context",
          |		   "elements": [
          |			   {
          |				   "type": "mrkdwn",
          |				   "text": "0 agree, 3 disagree, 5 comments"
          |			   }
          |		   ]
          |    }
          |  ]
          |}
          |""".stripMargin

      val context = SlackContext(elements = Seq(SlackText("0 agree, 3 disagree, 5 comments")))

      val message = SlackMessage(Seq(context))

      implicit val messageWriter: Writes[SlackMessage] = SlackMessageJsonWriter.slackSlackMessageWrites
      val json = Json.toJson(message)

      assetEquals(json.toString(), expected)
    }

    "create the correct Json payload for a Slack image" in {

      val expected =
        """
          |{
          |  "blocks": [
          |  {
          |  "type": "image",
          |  "title": {
          |    "type": "plain_text",
          |    "text": "Please enjoy this photo of a kitten"
          |  },
          |  "block_id": "image4",
          |  "image_url": "http://placekitten.com/500/500",
          |  "alt_text": "An incredibly cute kitten."
          |}
          |  ]
          |}
          |""".stripMargin

      val image = SlackImageBlock("http://placekitten.com/500/500", "An incredibly cute kitten.", block_id = Some("image4"), title = Some(SlackPlainText("Please enjoy this photo of a kitten")))

      val message = SlackMessage(Seq(image))

      implicit val messageWriter: Writes[SlackMessage] = SlackMessageJsonWriter.slackSlackMessageWrites
      val json = Json.toJson(message)

      assetEquals(json.toString(), expected)
    }
  }
}
