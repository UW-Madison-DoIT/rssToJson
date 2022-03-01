# Change log

## 1.0.10 - 2022-03-01

+ Backs off from overzealous too-large-response blocking
  introduced in 1.0.9:
  Do not reject responses indicating size `-1`,
  since apparently that indicates unknown size,
  not reliably unreasonably large size.

## 1.0.9 - 2022-02-15

+ When failing to parse a feed as UTF-8, tries again as US-ASCII.
+ When reading the remote feed, wrap it in a BOMInputStream
  to filter away any leading Byte Order Marker if present.
+ Reject responses larger than 1 million bytes,
  to reduce impact to system from unreasonably large RSS feed responses.
+ Requests for undefined feeds now result in 404 NOT FOUND status code rather than 500.

(Versions older than 1.0.9 predated this change log.)
