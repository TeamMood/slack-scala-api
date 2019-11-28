package com.teammood.slack.json

import com.teammood.slack.model.{SlackActions, SlackBlock, SlackBlockElement, SlackButtonElement, SlackButtonStyle, SlackContext, SlackDangerButtonStyle, SlackDivider, SlackImageElement, SlackMessage, SlackPlainText, SlackPrimaryButtonStyle, SlackSection, SlackText}
import play.api.libs.json.{JsPath, JsSuccess, Json, Reads, __}
import play.api.libs.functional.syntax._

object SlackMessageJsonReader {

  implicit val slackPlainTextReads: Reads[SlackPlainText] = Json.reads[SlackPlainText]
  implicit val slackTextReads: Reads[SlackText] = Json.reads[SlackText]

  implicit val slackButtonStyleReads: Reads[SlackButtonStyle] =  (
    __.read[String] and
      __.json.pick
    ).tupled map { case (slackBlockType, js) =>
    slackBlockType.toLowerCase match {
      case "primary" => SlackPrimaryButtonStyle
      case "danger" => SlackDangerButtonStyle
    }
  }

  implicit val slackImageElementReads: Reads[SlackImageElement] = Json.reads[SlackImageElement]
  implicit val slackButtonElementReads: Reads[SlackButtonElement] = Json.reads[SlackButtonElement]
  implicit val slackBlockElementReads: Reads[SlackBlockElement] =  (
    (__ \ 'type).read[String] and
      __.json.pick
    ).tupled flatMap { case (slackBlockType, js) =>
    slackBlockType.toLowerCase match {
      case "image" => Reads { _ => Json.fromJson[SlackImageElement](js) } map { c => c: SlackImageElement }
      case "button" => Reads { _ => Json.fromJson[SlackButtonElement](js) } map { c => c: SlackButtonElement }
      case "plain_text" => Reads { _ => Json.fromJson[SlackPlainText](js) } map { c => c: SlackPlainText }
      case "mrkdwn" => Reads { _ => Json.fromJson[SlackText](js) } map { c => c: SlackText }
    }
  }

  implicit val slackSectionReads: Reads[SlackSection] = (
    (JsPath \ "text").readNullable[SlackText] and
      (JsPath \ "fields").readNullable[Seq[SlackText]] and
      (JsPath \ 'accessory).readNullable[SlackBlockElement]
    ) { (text, fields, accessory) =>
    SlackSection(text, fields.getOrElse(Seq.empty), accessory)
  }
  implicit val slackActionsReads: Reads[SlackActions] = Json.reads[SlackActions]
  implicit val slackContextReads: Reads[SlackContext] = Json.reads[SlackContext]
  implicit val slackBlockReads: Reads[SlackBlock] =  (
    (__ \ 'type).read[String] and
      __.json.pick
    ).tupled flatMap { case (slackBlockType, js) =>
    slackBlockType.toLowerCase match {
      case "divider" => Reads { _ => JsSuccess(SlackDivider())}
      case "section" => Reads { _ => Json.fromJson[SlackSection](js) } map { c => c: SlackSection }
      case "actions" => Reads { _ => Json.fromJson[SlackActions](js) } map { c => c: SlackActions }
      case "context" => Reads { _ => Json.fromJson[SlackContext](js) } map { c => c: SlackContext }
    }
  }

  implicit val slackMessageReads: Reads[SlackMessage] = Json.reads[SlackMessage]
}
