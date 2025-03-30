package todolist

import io.ktor.server.plugins.BadRequestException
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator
import java.util.*

val validator: Validator =
    Validation.byDefaultProvider()
        .configure()
        .messageInterpolator(ParameterMessageInterpolator(emptySet(), Locale.ENGLISH, false))
        .buildValidatorFactory()
        .validator

/**
 * Validate an object using
 * [Jakarta Bean Validation](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/beanvalidation/bean-validation/bean-validation.html).
 */
fun <T : Any> T.validate() {
    val errors = validator.validate(this)
    if (errors.isNotEmpty()) {
        throw BadRequestException(errors.joinToString("\n") { "Property '${it.propertyPath}': ${it.message}" })
    }
}
