package com.teammood.slack.json

import com.teammood.slack.model.{SlackActions, SlackButtonElement, SlackContext, SlackDangerButtonStyle, SlackImageBlock, SlackMessage, SlackSection}
import org.specs2.execute.Result
import org.specs2.mutable.Specification
import play.api.libs.json.{JsError, JsSuccess, Json}

class SlackMessageJsonReaderSpec extends Specification {

  "SlackMessageJsonReader" should {

    "parse a json with a Slack section" in {

      val json =
        """
          |{
          |  "blocks": [
          |    {
          |      "text": {
          |        "type": "mrkdwn",
          |        "text": "Hello World!"
          |      },
          |      "type": "section"
          |    }
          |  ]
          |}
          |""".stripMargin

      import SlackMessageJsonReader._

      val result: Result = Json.parse(json).validate[SlackMessage] match {
        case JsSuccess(value, _) =>
          value.blocks.size must beEqualTo(1) and
            (value.blocks.head must beAnInstanceOf[SlackSection])
        case JsError(errors) => failure(errors.mkString("\n"))
      }
      result
    }

    "parse a json with a Slack actions section" in {

      val json =
        """
          |{
          |  "blocks": [
          |    {
          |      "elements": [
          |        {
          |          "text": {
          |            "type": "plain_text",
          |            "text": "Excellent"
          |          },
          |          "type": "button",
          |          "value": "excellent"
          |        },
          |        {
          |          "text": {
          |            "type": "plain_text",
          |            "text": "Good"
          |          },
          |          "type": "button",
          |          "value": "good"
          |        },
          |        {
          |          "text": {
          |            "type": "plain_text",
          |            "text": "Average"
          |          },
          |          "type": "button",
          |          "value": "average"
          |        },
          |        {
          |          "text": {
          |            "type": "plain_text",
          |            "text": "Hard"
          |          },
          |          "type": "button",
          |          "value": "hard"
          |        },
          |        {
          |          "text": {
          |            "type": "plain_text",
          |            "text": "Bad"
          |          },
          |          "type": "button",
          |          "value": "bad"
          |        }
          |      ],
          |      "type": "actions",
          |      "block_id": "anything"
          |    }
          |  ]
          |}
          |""".stripMargin

      import SlackMessageJsonReader._

      val result: Result = Json.parse(json).validate[SlackMessage] match {
        case JsSuccess(value, _) =>
          value.blocks.size must beEqualTo(1) and
          (value.blocks.head must beAnInstanceOf[SlackActions])
        case JsError(errors) => failure(errors.mkString("\n"))
      }
      result
    }

    "parse a json with a Slack danger button" in {

      val json =
        """
          |{
          |	"blocks": [
          |		{
          |			"type": "actions",
          |     "block_id": "a block id",
          |			"elements": [
          |				{
          |					"type": "button",
          |					"text": {
          |						"type": "plain_text",
          |						"text": "Excellent"
          |					},
          |					"value": "excellent",
          |         "style": "danger"
          |				}
          |			]
          |		}
          |	]
          |}
          |""".stripMargin

      import SlackMessageJsonReader._

      val result: Result = Json.parse(json).validate[SlackMessage] match {
        case JsSuccess(value, _) =>
          value.blocks.size must beEqualTo(1) and
            (value.blocks.head must beAnInstanceOf[SlackActions]) and
            (value.blocks.head.asInstanceOf[SlackActions].elements.head.asInstanceOf[SlackButtonElement].style must beEqualTo(Some(SlackDangerButtonStyle)))
        case JsError(errors) => failure(errors.mkString("\n"))
      }
      result
    }

    "parse a json with a Slack context" in {

      val json =
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

      import SlackMessageJsonReader._

      val result: Result = Json.parse(json).validate[SlackMessage] match {
        case JsSuccess(value, _) =>
          value.blocks.size must beEqualTo(1) and
            (value.blocks.head must beAnInstanceOf[SlackContext])
        case JsError(errors) => failure(errors.mkString("\n"))
      }
      result
    }

    "parse a json with a Slack image" in {

      val json =
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

      import SlackMessageJsonReader._

      val result: Result = Json.parse(json).validate[SlackMessage] match {
        case JsSuccess(value, _) =>
          value.blocks.size must beEqualTo(1) and
            (value.blocks.head must beAnInstanceOf[SlackImageBlock]) and
            (value.blocks.head.asInstanceOf[SlackImageBlock].image_url must beEqualTo("http://placekitten.com/500/500"))
        case JsError(errors) => failure(errors.mkString("\n"))
      }
      result
    }
  }
}
