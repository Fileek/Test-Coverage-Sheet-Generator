package entity

import com.gitlab.mvysny.konsumexml.Konsumer

data class Method(
    val name: String,
    val desc: String,
    val line: String,
    val counter: List<Counter>
) {
    companion object {
        fun xml(k: Konsumer): Method {
            k.checkCurrent("method")
            return with(k.attributes) {
                Method(
                    name = getValue("name"),
                    desc = getValue("desc"),
                    line = getValue("line"),
                    counter = k.children("counter") { Counter.xml(this) }
                )
            }
        }
    }
}
