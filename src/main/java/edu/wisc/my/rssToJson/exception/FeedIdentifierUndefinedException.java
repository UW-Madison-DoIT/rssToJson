package edu.wisc.my.rssToJson.exception;

/**
 * Exceptional case that a feed identifier had no definition.
 */
public class FeedIdentifierUndefinedException extends RuntimeException {

  public FeedIdentifierUndefinedException(String feedIdentifier) {
    super ("Found no definition for feed identifier "  + feedIdentifier);
  }
}
