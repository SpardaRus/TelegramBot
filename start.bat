set WORK_DIR=disk:\folder_jar_file
set INSTANCE_NAME=name_jar_file.jar
cd %WORK_DIR%
java -Dlogback.configurationFile=config\logback.groovy -jar %INSTANCE_NAME%