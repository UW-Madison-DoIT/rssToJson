# rssToJson

Converts RSS feeds to `JSON` (suitable for rendering with e.g. the RSS [widget type][angularjs-portal widgets docs] in [AngularJS-portal][]).

There is also the functionality to plug in a custom XML for feeds which are not in a standard rss format.

Intended for deployment as a "microservice".

## Confidence-inspiring badges

[![Dependency Status](https://dependencyci.com/github/UW-Madison-DoIT/rssToJson/badge)](https://dependencyci.com/github/UW-Madison-DoIT/rssToJson)

## Configuration

In `endpoint.properties`

declare key-value pairs

where the key is an arbitrary String identifying the feed

and the value is the URL of the associated RSS feed.

## Usage

`/{webapp-name}/rssTransform/prop/{key}`

as in

```xml
  <portlet-definition>
    <portlet-preference>
      <name>widgetType</name>
      <value>rss</value>
    </portlet-preference>
    <portlet-preference>
      <name>widgetURL</name>
      <value>/rss-to-json/rssTransform/prop/campus-news</value>
    </portlet-preference>
    <portlet-preference>
      <name>widgetConfig</name>
      <value><![CDATA[{ "lim" : 4 }]]></value>
    </portlet-preference>
    <portlet-preference>
      <name>content</name>
      <readOnly>false</readOnly>
      <value><![CDATA[
        <p>
          <a href="http://xkcd.com/"
            target="_blank"
            rel="noopener noreferrer">XKCD</a>
        </p>.]]>
      </value>
    </portlet-preference>
  </portlet-definition>
```

returns something like

```json
{
  "status":"ok",
  "feed":{
    "title":"Campus news – News",
    "link":"http://news.wisc.edu",
    "description":""},
  "items":[
    {"item":{
      "title":"Students make finals of ‘Team Ninja Warrior’",
      "link":"http://www.jsonline.com/story/life/green-sheet/2016/12/18/hartland-native-uw-madison-students-compete-team-ninja-warrior/95516194/",
      "description":"Three UW students – \"Science Ninja\" Zach Kemmerer, pole vaulter Taylor Amann and \"R.A. Ninja\" Andrew Philibeck – made it to the finals of \"Team Ninja Warrior College Madness,\" airing Dec. 20."}
    },
    {"item":{
      "title":"Astronaut Lovell tells grads: ‘We are all the crew of spaceship Earth’",
      "link":"http://news.wisc.edu/astronaut-lovell-tells-grads-we-are-all-the-crew-of-spaceship-earth/",
      "description":"More than 1,000 students celebrated winter commencement Sunday at the Kohl Center. Astronaut James Lovell delivered the charge to the graduates."}
    },
    {"item":{
      "title":"Astronaut James Lovell’s commencement speech",
      "link":"http://news.wisc.edu/astronaut-james-lovells-commencement-speech/",
      "description":"Read the complete text of Capt. James A. Lovell's commencement address to 2016 winter graduates."}
    }
  ]
}
```
##Custom Filters
`/{webapp-name}/rssTransform/prop/{key}/xml`

Custom filters allow you to consume an xml feed which is not in standard rss format, and return it as json using custom business logic you provide. 

As in the standard rss processor, `{key}` is the string identifying your feed. To utilize the custom filter, create a class which implements the iFilter interface. 

Give your class the case-insensitive name of your feed, plus the word "filter".. i.e. if your endpoint is `sports=http://www.ncaa.com/news/ncaa/d1/rss.xml`, then your class would be named `SportsFilter`.



[AngularJS-portal]: https://github.com/UW-Madison-DoIT/angularjs-portal
[angularjs-portal widgets docs]: http://uw-madison-doit.github.io/angularjs-portal/latest/#/md/widgets
