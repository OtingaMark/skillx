package com.skillx.core.validation

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EmailValidatorTest {
    @Test fun validEmail() = assertTrue(EmailValidator.isValid("user@example.com"))
    @Test fun validEmailWithSubdomain() = assertTrue(EmailValidator.isValid("user@mail.example.com"))
    @Test fun invalidNoAt() = assertFalse(EmailValidator.isValid("userexample.com"))
    @Test fun invalidNoDomain() = assertFalse(EmailValidator.isValid("user@"))
    @Test fun invalidEmpty() = assertFalse(EmailValidator.isValid(""))
    @Test fun invalidBlank() = assertFalse(EmailValidator.isValid("  "))
}
