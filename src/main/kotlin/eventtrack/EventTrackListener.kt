package eventtrack

import benchmark.BenchmarkContext
import okhttp3.Call
import okhttp3.EventListener
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors

/**
 * @author nzooherd <nzooherd@kuaishou.com>
 * Created on 2021/6/27
 */
class EventTrackListener(private val context: BenchmarkContext):EventListener() {

    private val executor = Executors.newFixedThreadPool(16)
    private val logFile = File("./metaperf/${context.strategyName}")
    init {
        logFile.writeText("request_id,time,event\n")
    }

    private fun record(call: Call, time: Long, httpLifeCycle: HttpLifeCycle, vararg args: String) {
        if (!needRecord(call)) {
            return
        }
         executor.submit {
             val timePoint = context.timePoint(time)
             val requestId = getRequestId(call)
             when(httpLifeCycle) {
                 HttpLifeCycle.CALL_START -> {
                     timePoint.addRequest()
                 }
                 HttpLifeCycle.RESPONSE_BODY_END -> {
                     timePoint.addSuccessRequest()
                 }
                 HttpLifeCycle.REQUEST_FAILED -> {
                    timePoint.addFailureRequest(500)
                 }
                 else -> {}
             }
             synchronized(logFile) {
                 logFile.appendText("$requestId, $time, ${httpLifeCycle.name.toLowerCase()}\n")
             }
         }
    }

    override fun callStart(call: Call) {
        super.callStart(call)
        record(call, System.currentTimeMillis(), HttpLifeCycle.CALL_START)
    }

    override fun requestBodyStart(call: Call) {
        super.requestBodyStart(call)
        record(call, System.currentTimeMillis(), HttpLifeCycle.REQUEST_BODY_START)
    }


    override fun requestBodyEnd(call: Call, byteCount: Long) {
        super.requestBodyEnd(call, byteCount)
        record(call, System.currentTimeMillis(), HttpLifeCycle.REQUEST_BODY_END,)
    }

    override fun responseHeadersStart(call: Call) {
        super.responseHeadersStart(call)
        record(call, System.currentTimeMillis(), HttpLifeCycle.RESPONSE_HEADER_START)
    }

    override fun responseHeadersEnd(call: Call, response: Response) {
        super.responseHeadersEnd(call, response)
        record(call, System.currentTimeMillis(), HttpLifeCycle.RESPONSE_HEADER_END)
    }

    override fun responseBodyStart(call: Call) {
        super.responseBodyStart(call)
        record(call, System.currentTimeMillis(), HttpLifeCycle.RESPONSE_BODY_START)
    }

    override fun responseBodyEnd(call: Call, byteCount: Long) {
        super.responseBodyEnd(call, byteCount)
        record(call, System.currentTimeMillis(), HttpLifeCycle.RESPONSE_BODY_END)
    }

    override fun requestFailed(call: Call, ioe: IOException) {
        super.requestFailed(call, ioe)
        record(call, System.currentTimeMillis(), HttpLifeCycle.REQUEST_FAILED, )
    }

    private fun getRequestId(call: Call): String = call.request().header("X-Benchmark-Request-Id")!!

    private fun needRecord(call: Call): Boolean = call.request().header("X-Benchmark-Record") == "true"
}