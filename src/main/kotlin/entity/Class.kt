package entity

import com.gitlab.mvysny.konsumexml.Konsumer

data class Class(
    val name: String,
    val sourceFilename: String,
    val methods: List<Method>,
    val counters: List<Counter>,
) {
    companion object {
        fun xml(k: Konsumer): Class {
            k.checkCurrent("class")
            return with(k.attributes) {
                Class(
                    name = getValue("name"),
                    sourceFilename = getValue("sourcefilename"),
                    methods = k.children("method") { Method.xml(this) },
                    counters = k.children("counter") { Counter.xml(this) },
                )
            }
        }
    }
}

