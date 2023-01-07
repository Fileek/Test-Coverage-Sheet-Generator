package entity

import com.gitlab.mvysny.konsumexml.Konsumer

data class SourceFile(
    val name: String,
    val lines: List<Line>,
    val counters: List<Counter>,
) {
    companion object {
        fun xml(k: Konsumer): SourceFile {
            k.checkCurrent("sourcefile")
            return SourceFile(
                name = k.attributes.getValue("name"),
                lines = k.children("line") { Line.xml(this) },
                counters = k.children("counter") { Counter.xml(this) },
            )
        }
    }
}
