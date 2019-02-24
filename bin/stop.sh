#!/bin/bash
APP_LABEL=appid=Tool_Bot_1
APP_NAME=Tool_Bot-1.0.jar
pid=`ps -fe | grep $APP_LABEL | grep -v grep | tr -s " "|cut -d" " -f2`
if [ -z "$pid" ]
then echo -e "\e[00;31m$APP_NAME is not running\e[00m"
else
 #stop app
 echo -e "\e[00;32mStopping $APP_NAME\e[00m"
 kill $pid
fi