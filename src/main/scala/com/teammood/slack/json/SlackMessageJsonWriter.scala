package com.teammood.slack.json

import com.teammood.slack.model._
import play.api.libs.json.{Json, Writes}

object SlackMessageJsonWriter {

  implicit val slackSlackTextWrites: Writes[SlackText] = (o: SlackText) => Json.obj(
    "type" -> "mrkdwn",
    "text" -> o.text
  )

  implicit val slackSlackSlackPlainTextWrites: Writes[SlackPlainText] = (o: SlackPlainText) => Json.obj(
    "type" -> "plain_text",
    "text" -> o.text
  )

  implicit val slackSlackBlockElementWrites: Writes[SlackBlockElement] = (o: SlackBlockElement) => {

    val baseJson = Json.obj(
      "type" -> o.id)

    o match {
      case SlackImageElement(image_url, alt_text) =>
        baseJson ++ Json.obj(
          "image_url"-> image_url,
          "alt_text" -> alt_text
        )
      case SlackButtonElement(text, optionalValue, optionalUrl, optionalStyle) =>
        val value = optionalValue.map(v => Json.obj("value" -> v)).getOrElse(Json.obj())
        val style = optionalStyle.map(s => Json.obj("style" -> s.id)).getOrElse(Json.obj())
        val url = optionalUrl.map(u => Json.obj("url" -> u)).getOrElse(Json.obj())

        baseJson ++ Json.obj("text"-> text) ++ value ++ style ++ url
      case slackText: SlackText => slackSlackTextWrites.writes(slackText)
      case slackPlainText: SlackPlainText => slackSlackSlackPlainTextWrites.writes(slackPlainText)
      case _ => baseJson
    }
  }

  implicit val slackSlackBlockWrites: Writes[SlackBlock] = (o: SlackBlock) => {

    val baseJson = Json.obj(
      "type" -> o.id)

    o match {
      case SlackDivider() => baseJson
      case SlackActions(blockId, optionalElements) =>
        val elements = if (optionalElements.isEmpty) {
          Json.obj()
        } else {
          Json.obj("elements" -> optionalElements)
        }
        baseJson ++ Json.obj("block_id" -> blockId) ++ elements
      case SlackSection(optionalText, optionalFields, optionalAccessory) =>
        val text = optionalText.map(text => Json.obj("text" -> text)).getOrElse(Json.obj())
        val fields = if (optionalFields.isEmpty) {
          Json.obj()
        } else {
          Json.obj("fields" -> optionalFields)
        }
        val accessory = optionalAccessory.map(accessory => Json.obj("accessory" -> accessory)).getOrElse(Json.obj())

        baseJson ++ text ++ fields ++ accessory
      case SlackContext(optionalBlockId, optionalElements) =>
        val blockId = optionalBlockId.map(bid => Json.obj("block_id" -> bid)).getOrElse(Json.obj())
        val elements = if (optionalElements.isEmpty) {
          Json.obj()
        } else {
          Json.obj("elements" -> optionalElements)
        }
        baseJson ++ blockId ++ elements
      case SlackImageBlock(image_url, alt_text, optionalBlockId, optionalTitle) =>
        val blockId = optionalBlockId.map(bid => Json.obj("block_id" -> bid)).getOrElse(Json.obj())
        val title = optionalTitle.map(title => Json.obj("title" -> title)).getOrElse(Json.obj())

        baseJson ++ blockId ++ Json.obj("image_url" -> image_url, "alt_text" -> alt_text) ++ title
      case _ => baseJson
    }
  }

  implicit val slackSlackMessageWrites: Writes[SlackMessage] = (o: SlackMessage) => Json.obj(
    "blocks" -> o.blocks)

}
