#!/bin/bash
WORK_DIR=/folder_jar_file
APP_NAME=Tool_Bot-1.0.jar
APP_LABEL=Tool_Bot_1
JAVA_HOME=/folder_jre
JAVA_OPTS="$JAVA_OPTS -Dlogback.configurationFile=config/logback.groovy"
JAVA_OPTS="$JAVA_OPTS -Dlabel.appid=$APP_LABEL"

pid=`ps -fe | grep $APP_LABEL | grep -v grep | tr -s " "|cut -d" " -f2`

cd "$WORK_DIR"

if [ -n "$pid" ]
then echo -e "\e[00;31m$APP_NAME is already running (pid: $pid)\e[00m"
else
#start app
   echo -e "\e[00;32mStarting $APP_NAME\e[00m"
CMD="${JAVA_HOME}/bin/java -jar $JAVA_OPTS $APP_NAME"
$CMD >> "$WORK_DIR/logs/console.out" 2>&1 &
# if [ -n "$new_pid" ]
echo -e "\e[00;32m$APP_NAME is running with pid: $!\e[00m"
# else echo -e "\e[00;31m$APP_NAME is not running\e[00m"
fi