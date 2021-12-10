import java.io.File

/**
 * Gets the first illegal closing bracket or null if none is found.
 */
fun String.firstIllegalClosingOrNull(): Char? {
    val openings = mutableListOf<Char>()
    val openBrackets = listOf('(', '[', '{', '<')

    for (c in this) {
        if (c in openBrackets) {
            openings.add(c)
            continue
        }

        val lastBracket = openings.removeLastOrNull() ?: return c
        when (c) {
            ')' -> if (lastBracket != '(') return c
            ']' -> if (lastBracket != '[') return c
            '}' -> if (lastBracket != '{') return c
            '>' -> if (lastBracket != '<') return c
        }
    }

    return null
}

/**
 * Builds the required closing brackets that are probably missing. This method assumes that the input
 * is incomplete but not corrupted.
 */
fun String.checkRequiredClosingBrackets(): List<Char> {
    val openings = mutableListOf<Char>()
    val openBrackets = listOf('(', '[', '{', '<')

    for (c in this) {
        if (c in openBrackets) {
            openings.add(c)
            continue
        }

        openings.removeLast()
    }

    // Now that we have removed all the valid opening brackets, we just need to add the closing brackets, if any.
    val closings = openings.mapNotNull {
        when (it) {
            '(' -> ')'
            '[' -> ']'
            '{' -> '}'
            '<' -> '>'
            else -> null
        }
    }

    return closings.reversed()
}

/**
 * Removes the corrupted lines from the list of inputs. It's returning the same list reference.
 */
fun MutableList<String>.removeCorruptedLines(): MutableList<String> {
    this.removeIf { it.firstIllegalClosingOrNull() != null }
    return this
}

/**
 * Collects all the required additional closing brackets all in a single list.
 */
fun MutableList<String>.collectAdditionalClosingBrackets(): List<List<Char>> {
    return this.map { it.checkRequiredClosingBrackets() }
}

/**
 * Gets the middle element of the list.
 */
fun List<Long>.middleElement(): Long {
    return this[this.size / 2]
}

fun solvePart1(inputs: List<String>) {
    val pointsMap = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)

    // For each line input, determine if that line is corrupted and calculate the score.
    val score = inputs.mapNotNull { it.firstIllegalClosingOrNull() }.mapNotNull { pointsMap[it] }.sum()

    println("PART 1 ANSWER")
    println(score)
}

fun solvePart2(inputs: List<String>) {
    val pointsMap = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

    // For each line input, remove the corrupted lines, get the required closing brackets, then calculate the score.
    val score = inputs.toMutableList()
        .removeCorruptedLines()
        .collectAdditionalClosingBrackets()
        .map { it.fold(0L) { acc, c -> 5 * acc + pointsMap[c]!! } }
        .sorted()
        .middleElement()

    println("PART 2 ANSWER")
    println(score)
}

fun main() {
    val inputs = File("input.txt").readLines()

    solvePart1(inputs)
    solvePart2(inputs)
}