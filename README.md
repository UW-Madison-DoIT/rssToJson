# personalizedRedirection
A service to provide a customized URL based on the identity of the user. 

In the resources folder, a file called dataSources.json controls the mechanics of this service.

{"dataSources":[
{"appName":"demo", "attributeName":"uid", "dataSourceLocation":"sampleIds.csv", "dataSourceType":"CSV"},
{"appName":"whitefish", "attributeName":"whitefish", "dataSourceLocation":"nonexistent.json", "dataSourceType":"JSON"}
]}

appName: A parameter passed into the requestMapping, informing the service which application is calling.
attributeName: Which of the user's header attirbutes will be used as the key to find the matching url?
dataSourceLocation: This is currently configured to find a file on the classpath. Can be expanded to grab data from a URL or other source.
dataSourceType: Current functionality only supports a two column CSV file. This can be expanded to include other file types.

