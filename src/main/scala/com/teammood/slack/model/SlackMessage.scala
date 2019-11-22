package com.teammood.slack.model

import play.api.libs.json.{Json, Writes}

case class SlackMessage(blocks: Seq[SlackBlock] = Seq())

// https://api.slack.com/reference/block-kit/blocks
trait SlackBlock {
  val id: String
}
case class SlackSection(text: Option[SlackText] = None, fields: Seq[SlackText] = Seq(), accessory: Option[SlackBlockElement] = None) extends SlackBlock {
  override val id = "section"
}
case class SlackDivider() extends SlackBlock {
  override val id = "divider"
}
case class SlackActions(block_id: Option[String] = None, elements: Seq[SlackBlockElement] = Seq()) extends SlackBlock {
  override val id = "actions"
}

case class SlackPlainText(text: String)

case class SlackText(text: String) {
  def bold: SlackText = SlackText(s"*$text*")
}

object SlackText {
  def toLink(title: String, url: String) = s"<$url|$title>"
}

sealed trait SlackButtonStyle {
  val id: String
}
case object SlackPrimaryButtonStyle extends SlackButtonStyle {
  override val id: String = "primary"
}
case object SlackDangerButtonStyle extends SlackButtonStyle {
  override val id: String = "danger"
}

//https://api.slack.com/reference/block-kit/block-elements
trait SlackBlockElement {
  val id: String
}
case class SlackImageElement(image_url: String, alt_text: String) extends SlackBlockElement {
  override val id: String = "image"
}

case class SlackButtonElement(text: SlackPlainText, value: Option[String] = None, style: Option[SlackButtonStyle] = None) extends SlackBlockElement {
  override val id: String = "button"
}

object SlackMessage {

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
      case SlackButtonElement(text, optionalValue, optionalStyle) =>
        val value = optionalValue.map(v => Json.obj("value" -> v)).getOrElse(Json.obj())
        val style = optionalStyle.map(s => Json.obj("style" -> s.id)).getOrElse(Json.obj())

        baseJson ++ Json.obj("text"-> text) ++ value ++ style
      case _ => baseJson
    }
  }

  implicit val slackSlackBlockWrites: Writes[SlackBlock] = (o: SlackBlock) => {

    val baseJson = Json.obj(
      "type" -> o.id)

    o match {
      case SlackDivider() => baseJson
      case SlackActions(optionalBlockId, optionalElements) =>
        val blockId = optionalBlockId.map(bid => Json.obj("block_id" -> bid)).getOrElse(Json.obj())
        val elements = if (optionalElements.isEmpty) {
          Json.obj()
        } else {
          Json.obj("elements" -> optionalElements)
        }
        baseJson ++ blockId ++ elements
      case SlackSection(optionalText, optionalFields, optionalAccessory) =>
        val text = optionalText.map(text => Json.obj("text" -> text)).getOrElse(Json.obj())
        val fields = if (optionalFields.isEmpty) {
          Json.obj()
        } else {
          Json.obj("fields" -> optionalFields)
        }
        val accessory = optionalAccessory.map(accessory => Json.obj("accessory" -> accessory)).getOrElse(Json.obj())

        baseJson ++ text ++ fields ++ accessory
      case _ => baseJson
    }
  }

  implicit val slackSlackMessageWrites: Writes[SlackMessage] = (o: SlackMessage) => Json.obj(
    "blocks" -> o.blocks)
}
