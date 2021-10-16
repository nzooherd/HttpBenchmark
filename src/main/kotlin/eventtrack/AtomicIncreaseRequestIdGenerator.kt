package eventtrack

import okhttp3.Request
import java.util.concurrent.atomic.AtomicLong

/**
 * @author nzooherd <nzooherd@gmail.com>
 * Created on 6/27/2021
 */
class AtomicIncreaseRequestIdGenerator: RequestIdGenerator {
    override fun generateRequestId(request: Request) : String {
        return requestId.getAndAdd(1).toString()
    }
    fun generateRequestId() : String {
        return requestId.getAndAdd(1).toString()
    }
    companion object {
        val requestId = AtomicLong()
    }
}

val generator = AtomicIncreaseRequestIdGenerator()
fun exportGenerateRequestId() : String = generator.generateRequestId()