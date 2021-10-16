package eventtrack

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * @author nzooherd <nzooherd@gmail.com>
 * Created on 6/27/2021
 */
class EventTrackRequestInterceptor(private val requestIdGenerator: RequestIdGenerator): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        chain.request().headers().names()

        val originHeaders = chain.request().headers()
        val originHeaderMap = originHeaders.names().associateWith { originHeaders[it] }.toMutableMap()
        originHeaderMap[requestIdHeader] = requestIdGenerator.generateRequestId(chain.request())
        val newHeaders = Headers.of(originHeaderMap)
        setFinalStatic(chain.request(), Request::class.java.getDeclaredField("headers"), newHeaders)
        return chain.proceed(chain.request())
    }

    private fun setFinalStatic(`object`: Any, field: Field, newValue: Any?) {
        field.isAccessible = true
        val modifiersField: Field = Field::class.java.getDeclaredField("modifiers")
        modifiersField.isAccessible = true
        modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
        field.set(`object`, newValue)
    }

    companion object {
        const val requestIdHeader = "Request-Id"
    }
}