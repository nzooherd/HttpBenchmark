package eventtrack

/**
 * @author shiwenhao <shiwenhao@kuaishou.com>
 * Created on 2021/7/14
 */
enum class HttpLifeCycle {
    CALL_START,
    REQUEST_BODY_START,
    REQUEST_BODY_END,
    RESPONSE_HEADER_START,
    RESPONSE_HEADER_END,
    RESPONSE_BODY_START,
    RESPONSE_BODY_END,
    REQUEST_FAILED,
}