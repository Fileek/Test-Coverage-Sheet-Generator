package entity

import com.gitlab.mvysny.konsumexml.Konsumer

data class Counter(
    val type: String,
    val missed: String,
    val covered: String,
) {
    companion object {
        fun xml(k: Konsumer): Counter {
            k.checkCurrent("counter")
            return with(k.attributes) {
                Counter(
                    type = getValue("type"),
                    missed = getValue("missed"),
                    covered = getValue("covered"),
                )
            }
        }
    }
}