package eventtrack

import okhttp3.Request

/**
 * @author nzooherd <nzooherd@gmail.com>
 * Created on 6/27/2021
 */
interface RequestIdGenerator {
    fun generateRequestId(request: Request): String
}