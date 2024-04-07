package com.merveylcu.characters.presentation.util

import com.merveylcu.contract.INetworkError
import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.extension.RegisterExtension

internal open class BaseTestUtil {

    @JvmField
    @RegisterExtension
    var mainCoroutineExtension = MainCoroutineExtension()

    val mockNetworkError = spyk<INetworkError> {
        every { message } returns "test_error_message"
        every { code } returns 403
    }
}
