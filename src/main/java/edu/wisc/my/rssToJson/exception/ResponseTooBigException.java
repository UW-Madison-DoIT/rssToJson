package edu.wisc.my.rssToJson.exception;

public class ResponseTooBigException extends RuntimeException {
  public ResponseTooBigException(String identifier, long size, long maximum) {
    super("Feed " + identifier + " responded with " + size + " bytes, but was only allowed " + maximum + " bytes.");
  }
}
