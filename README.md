A simple scala wrapped for the Slack API.


## Publish to bintray

Add your bintray credentials in ~/.bintray/.credentials
or in sbt, use `bintrayChangeCredentials`

Then, to publish, just use: `+publish` in sbt (the + sign for cross building against multiple Scala versions)

(More details available at https://github.com/sbt/sbt-bintray)
