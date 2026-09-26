package com.skillx.core.validation

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PasswordValidatorTest {
    @Test fun validPassword() = assertTrue(PasswordValidator.isValid("abc123"))
    @Test fun validLongPassword() = assertTrue(PasswordValidator.isValid("a very long password indeed"))
    @Test fun invalidTooShort() = assertFalse(PasswordValidator.isValid("abc"))
    @Test fun invalidEmpty() = assertFalse(PasswordValidator.isValid(""))
    @Test fun exactMinimumLength() = assertTrue(PasswordValidator.isValid("abcdef"))
    @Test fun oneCharShort() = assertFalse(PasswordValidator.isValid("abcde"))
}
