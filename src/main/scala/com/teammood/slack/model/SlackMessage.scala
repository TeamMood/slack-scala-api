package com.teammood.slack.model

case class SlackMessage(blocks: Seq[SlackBlock] = Seq())

// https://api.slack.com/reference/block-kit/blocks
trait SlackBlock {
  val id: String
}
case class SlackSection(text: Option[SlackText] = None, fields: Seq[SlackText] = Seq(), accessory: Option[SlackBlockElement] = None) extends SlackBlock {
  override val id = "section"
}

case class SlackContext(block_id: Option[String] = None, elements: Seq[SlackBlockElement] = Seq()) extends SlackBlock {
  override val id = "context"
}
case class SlackDivider() extends SlackBlock {
  override val id = "divider"
}
case class SlackActions(block_id: Option[String] = None, elements: Seq[SlackBlockElement] = Seq()) extends SlackBlock {
  override val id = "actions"
}
case class SlackImageBlock(image_url: String, alt_text: String, block_id: Option[String] = None, title: Option[SlackPlainText] = None) extends SlackBlock {
  override val id = "image"
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

case class SlackButtonElement(text: SlackPlainText, value: Option[String] = None, url: Option[String] = None, style: Option[SlackButtonStyle] = None) extends SlackBlockElement {
  override val id: String = "button"
}

case class SlackPlainText(text: String) extends SlackBlockElement {
  override val id: String = "plain_text"
}

case class SlackText(text: String) extends SlackBlockElement {
  override val id: String = "mrkdwn"

  def bold: SlackText = SlackText(s"*$text*")
}

object SlackText {
  def toLink(title: String, url: String) = s"<$url|$title>"
}