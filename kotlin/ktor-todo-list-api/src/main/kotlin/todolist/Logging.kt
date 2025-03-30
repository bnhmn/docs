package todolist

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.companionObject

/** Extend any class with the ability to get a logger */
val <T : Any> T.log: Logger
    get() {
        return LoggerFactory.getLogger(unwrapCompanionClass(this.javaClass).name)
    }

fun <T : Any> unwrapCompanionClass(cls: Class<T>): Class<*> {
    return cls.enclosingClass?.takeIf { cls.enclosingClass.kotlin.companionObject?.java == cls }
        ?: cls
}
