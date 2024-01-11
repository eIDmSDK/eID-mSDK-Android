package sk.plaut.base.exceptions

import java.lang.RuntimeException
import java.util.*

sealed class BaseException : RuntimeException {
    var code: Int = 0
        private set
    override var message: String? = null
    var data: String? = null
        private set

    constructor(message: String?) : super(message) {
        this.message = message
    }

    constructor(code: Int, message: String?) : super(String.format(Locale.getDefault(), "%d %s", code, message)) {
        this.code = code
        this.message = message
    }

    constructor(message: String, data: String?) : super(String.format(Locale.getDefault(), "%s %s", message, data)) {
        this.message = message
        this.data = data
    }

    constructor(code: Int, message: String?, data: String) : super(String.format(Locale.getDefault(), "%d %s %s", code, message, data)) {
        this.code = code
        this.message = message
        this.data = data
    }

    constructor(code: Int, throwable: Throwable) : super(String.format(Locale.getDefault(), "%d %s", code, throwable.message), throwable) {
        this.code = code
        this.message = throwable.message
    }

    constructor(throwable: Throwable) : super(throwable) {
        this.message = throwable.message
    }

    constructor(message: String?, throwable: Throwable) : super(message, throwable) {
        this.message = message
    }

    constructor(code: Int, message: String?, throwable: Throwable) : super(String.format(Locale.getDefault(), "%d %s", code, message), throwable) {
        this.code = code
        this.message = message
    }
}

class ApplicationException : BaseException {
    constructor(message: String?) : super(message)
    constructor(code: Int, message: String?) : super(code, message)
}

class ServerException : BaseException {
    constructor(message: String?) : super(message)
    constructor(code: Int, message: String?) : super(code, message)
}

class UnexpectedException : BaseException {
    constructor(message: String?) : super(message)
    constructor(message: String?, throwable: Throwable) : super(message, throwable)
    constructor(throwable: Throwable) : super(throwable)
}