import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.ThresholdFilter

import static ch.qos.logback.classic.Level.*

String INSTANCE_NAME = "Tool bot"
String PATH_LOG = "var/logs"
String FILE_PATTERN = "%d{yyyy-MM-dd}_%i.log.gz"
String MAX_FILE_SIZE = "100MB"
String ENCODER_PATTERN = "%date{dd/MM/yy HH:mm:ss.SSS} %level [%thread] %logger{10}: %msg%n"
int MAX_HISTORY = 31

appender("DEBUG_INFO_FILE", RollingFileAppender) {
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${PATH_LOG}/${INSTANCE_NAME}-debug-${FILE_PATTERN}"
        timeBasedFileNamingAndTriggeringPolicy(SizeAndTimeBasedFNATP) {
            maxFileSize = MAX_FILE_SIZE
        }
        maxHistory = MAX_HISTORY
    }
    encoder(PatternLayoutEncoder) {
        pattern = "${ENCODER_PATTERN}"
    }
    filter(ThresholdFilter) {
        level = DEBUG
    }
}

appender("CONSOLE_OUT", FileAppender) {
    file="${PATH_LOG}/console.out"
    encoder(PatternLayoutEncoder) {
        pattern = "${ENCODER_PATTERN}"
    }
}

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "${ENCODER_PATTERN}"
    }
    filter(ThresholdFilter) {
        level = ERROR
    }
}

logger("com.spardarus.bot.Application", DEBUG, ["CONSOLE_OUT"], false)
logger("com.spardarus.bot", DEBUG, ["DEBUG_INFO_FILE"], false)
root(ERROR, ["STDOUT"])