#!/bin/bash
if [ "$#" = "0" ]; then
 mvn -Djava.awt.headless=true clean install
else
 mvn -Djava.awt.headless=true $@
fi

mvn -Djava.awt.headless=true tomcat7:redeploy

unamestr=`uname`
if [[ "$unamestr" == 'Linux' ]]; then
 notify-send "build complete for rssToJson"
elif [[ "$unamestr" == 'Darwin' ]]; then
 osascript -e 'display notification "rssToJson build.sh finished" with title "rssToJson deployed" sound name "Hero"'
fi
