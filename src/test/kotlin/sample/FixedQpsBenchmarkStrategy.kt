package sample

import benchmark.BenchmarkContext
import benchmark.BenchmarkStrategy
import benchmark.CurrentSecondDo

/**
 * @author nzooherd <nzooherd@gmail.com>
 * Created on 6/29/2021
 */
class FixedQpsBenchmarkStrategy: BenchmarkStrategy() {
    override fun secondDo(context: BenchmarkContext): CurrentSecondDo {
        return object : CurrentSecondDo {
            override suspend fun doSomething(context: BenchmarkContext) {

            }
        }
    }

    override fun end(context: BenchmarkContext): Boolean {
        return context.relativeSecond >= 10 * 60
    }
}