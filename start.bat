set WORK_DIR=disk:\folder_jar_file
set INSTANCE_NAME=Tool_Bot-1.0.jar
cd %WORK_DIR%
java -Dlogback.configurationFile=config\logback.groovy -jar %INSTANCE_NAME%