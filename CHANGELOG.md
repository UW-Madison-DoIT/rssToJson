# Change log

## Next (1.0.9?)

+ When reading the remote feed, wrap it in a BOMInputStream
  to filter away any leading Byte Order Marker if present.
+ Reject responses larger than 1 million bytes,
  to reduce impact to system from unreasonably large RSS feed responses.

(Versions older than 1.0.9 predated this change log.)
