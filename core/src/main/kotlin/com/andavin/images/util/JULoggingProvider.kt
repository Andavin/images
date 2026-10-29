package com.andavin.images.util

import org.tinylog.Level
import org.tinylog.format.MessageFormatter
import org.tinylog.provider.ContextProvider
import org.tinylog.provider.LoggingProvider
import org.tinylog.provider.NopContextProvider
import java.util.function.Supplier
import java.util.logging.Logger

class JULoggingProvider : LoggingProvider {

    private val ctx = NopContextProvider()

    override fun getContextProvider(): ContextProvider = ctx

    override fun getMinimumLevel(): Level = Level.TRACE

    override fun getMinimumLevel(tag: String?): Level = Level.TRACE

    override fun isEnabled(depth: Int, tag: String?, level: Level) = logger.isLoggable(level.jul)

    override fun isEnabled(loggerClassName: String?, tag: String?, level: Level) = logger.isLoggable(level.jul)

    override fun log(
        depth: Int, tag: String?, level: Level, exception: Throwable?,
        formatter: MessageFormatter?, obj: Any?, vararg arguments: Any?
    ) = forward(level, exception, formatter, obj, arguments)

    override fun log(
        loggerClassName: String?, tag: String?, level: Level, exception: Throwable?,
        formatter: MessageFormatter?, obj: Any?, vararg arguments: Any?
    ) = forward(level, exception, formatter, obj, arguments)

    override fun shutdown() {
    }

    private fun forward(
        level: Level, exception: Throwable?,
        formatter: MessageFormatter?,
        obj: Any?, arguments: Array<out Any?>
    ) {

        val julLevel = level.jul
        if (!logger.isLoggable(julLevel)) {
            return
        }

        val message = (obj as? Supplier<*>)?.get() ?: obj
        val text = if (formatter != null && arguments.isNotEmpty()) {
            formatter.format(message.toString(), arrayOf(*arguments))
        } else {
            message?.toString()
        }

        logger.log(julLevel, text, exception)
    }

    companion object {
        @Volatile
        var logger: Logger = Logger.getLogger("Images")
    }
}

private val Level.jul: java.util.logging.Level
    get() = when (this) {
        Level.TRACE -> java.util.logging.Level.FINER
        Level.DEBUG -> java.util.logging.Level.FINE
        Level.INFO -> java.util.logging.Level.INFO
        Level.WARN -> java.util.logging.Level.WARNING
        Level.ERROR -> java.util.logging.Level.SEVERE
        Level.OFF -> java.util.logging.Level.OFF
    }