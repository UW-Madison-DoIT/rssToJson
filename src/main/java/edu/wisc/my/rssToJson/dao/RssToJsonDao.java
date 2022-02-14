package edu.wisc.my.rssToJson.dao;

import com.rometools.rome.feed.synd.SyndFeed;

public interface RssToJsonDao{

  /**
   * Get the SyndFeed with the given identifier.
   *
   * @param feedEndpoint identifier for the feed
   * @return a SyndFeed for the given endpoint
   * @throws edu.wisc.my.rssToJson.exception.FeedIdentifierUndefinedException if the DAO is not aware of a feed with the given identifier
   * @throws edu.wisc.my.rssToJson.exception.ResponseTooBigException if the feed response is unreasonably large
   */
  public SyndFeed getRssFeed(String feedEndpoint);

}
