import java.util.*
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

fun main() {
    val scanner = Scanner(System.`in`)
    println("Введите а0:")
    val a0 = scanner.nextFloat()
    println("Введите b0:")
    val b0 = scanner.nextFloat()
    println("Введите eps:")
    val eps = scanner.nextFloat()

    ChordMethod(a0, b0, eps).run {
        solve { solution, f, df ->
            if (solution == null) {
                println("Деление на ноль! Решение не было получено.")
                return@solve
            }
            println("Решение получено!")
            println("x* = $solution, f(x*) = ${f.round()}, df(x*) = ${df.round()}")
        }
    }
}

class ChordMethod(
    private var a: Float,
    private var b: Float,
    private val eps: Float,
) {
    fun solve(callback: (Float?, Float, Float) -> Unit) {
        if (df(a) * df(b) < 0) {
            var e: Float
            var currentY: Float
            do {
                currentY = a - (df(a) / (df(a) - df(b))) * (a - b)
                if (abs(currentY) == Float.POSITIVE_INFINITY) {
                    callback.invoke(null, 0f, 0f)
                    return
                }
                e = df(currentY)
                if (e > 0) b = currentY else a = currentY
            } while (e > eps)
            callback.invoke(currentY, f(currentY), df(currentY))
        } else {
            if (df(a) == 0.0f)
                callback.invoke(a, f(a), df(a))
            if (df(b) == 0.0f)
                callback.invoke(b, f(b), df(b))
            if (df(a) > 0 && df(b) > 0)
                callback.invoke(a, f(a), df(a))
            else
                callback.invoke(b, f(b), df(b))
        }
    }

    private fun f(x: Float) = (x - 2).pow(4)

    private fun df(x: Float) = 4 * (x - 2).pow(3)
}

fun Float.round(): Double {
    var multiplier = 1.0
    repeat(3) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}