#!/bin/bash
if [ "$#" = "0" ]; then
 mvn -Djava.awt.headless=true clean install
else
 mvn -Djava.awt.headless=true $@
fi

mvn -Djava.awt.headless=true tomcat7:redeploy

unamestr=`uname`
if [[ "$unamestr" == 'Linux' ]]; then
 notify-send "build complete for personalizedRedirection"
elif [[ "$unamestr" == 'Darwin' ]]; then
 osascript -e 'display notification "personalizedRedirection build.sh finished" with title "personalizedRedirection deployed" sound name "Hero"'
fi
