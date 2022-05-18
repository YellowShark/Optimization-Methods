import kotlin.math.pow
import kotlin.math.sqrt

const val N = 2

class PowellMethod(private val eps: Float = 0.001f) {
    fun solve() {
        var k = 0

        val q = arrayOf(0f to 1f, 1f to 0f, 0f to 1f)

        val x = arrayOfNulls<Pair<Float, Float>>(N + 2)
        x[0] = (0.5f to 1f)

        val y = arrayOfNulls<Pair<Float, Float>>(N + 2)
        y[0] = x[0]

        var solved = powellMethod(k, eps, y, x, q)
        while (k != N) {
            if (solved) return
            repeat(N - 1) { j -> q[j] = q[j + 1] }
            q[0] = y[N + 1]!!.minus(y[1]!!)
            q[N] = q[0]
            y[0] = x[k + 1]
            k++
            solved = powellMethod(k, eps, y, x, q)
        }
        println("Failure! Answer is not found.\n${x.last()}")
    }

    private fun powellMethod(
        k: Int,
        eps: Float,
        y: Array<Pair<Float, Float>?>,
        x: Array<Pair<Float, Float>?>,
        q: Array<Pair<Float, Float>>,
    ): Boolean {
        var i = 0
        do {
            y[i + 1] = calculateY(i, q, y)
            i++
        } while (i != N + 1)
        if (y[N + 1].equals2(y[1])) {
            println("y[${N + 1}] = y[1]")
            println("Solved! Answer is x* = ${y[N + 1].toString()}\n$k iterations")
            return true
        }
        x[k + 1] = y[N + 1]
        if (norm(x[k]!! - (x[k + 1]!!)) < eps) {
            println("||x[$k] - x[${k + 1}|| < eps")
            println("Solved! Answer is x* = ${x[k + 1].toString()}")
            return true
        }
        return false
    }

    private fun f(x: Pair<Float, Float>) =
        (x.first - 1.5f).pow(3) + (x.second + 2.5f).pow(3)

    private fun calculateY(i: Int, q: Array<Pair<Float, Float>>, y: Array<Pair<Float, Float>?>): Pair<Float, Float> {
        val alpha = computeAlpha(y[i]!!, q[i])
        return y[i]!! + (q[i].multiply(alpha))
    }

    private fun computeAlpha(y: Pair<Float, Float>, q: Pair<Float, Float>): Float {
        return enumMethod { alpha ->
            f(y + q.multiply(alpha))
        }
    }

    private fun enumMethod(f: (Float) -> Float): Float {
        val a0 = -10
        val b0 = 10
        val n = b0 - a0 - 1
        val x = arrayOfNulls<Float>(n * 100)
        val funs = arrayOfNulls<Float>(n * 100)

        repeat(n * 100 - 1) { i ->
            val xs = a0.toFloat() + (i).toFloat() * ((b0 - a0).toFloat() / ((n).toFloat() * 100))
            x[i] = xs
            funs[i] = f(x[i]!!)
        }

        val fMin = funs.minByOrNull { it ?: 100000f }
        val xMin = x[funs.indexOfFirst { it == fMin }]
        return xMin!!
    }

    private fun Pair<Float, Float>.multiply(alpha: Float): Pair<Float, Float> =
        first * alpha to second * alpha

    operator fun Pair<Float, Float>.plus(other: Pair<Float, Float>): Pair<Float, Float> =
        first + other.first to second + other.second

    operator fun Pair<Float, Float>.minus(other: Pair<Float, Float>): Pair<Float, Float> =
        this + (other.multiply(-1f))

    private fun norm(v: Pair<Float, Float>): Float =
        sqrt(v.first.pow(2) + v.second.pow(2))

    private fun Pair<Float, Float>?.equals2(other: Pair<Float, Float>?): Boolean =
        this?.first == other?.first && this?.second == other?.second

}