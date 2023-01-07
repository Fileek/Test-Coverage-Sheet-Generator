package entity

import com.gitlab.mvysny.konsumexml.Konsumer

data class Line(
    val nr: String,
    val mi: String,
    val ci: String,
    val mb: String,
    val cb: String,
) {
    companion object {
        fun xml(k: Konsumer): Line {
            k.checkCurrent("line")
            return with(k.attributes) {
                Line(
                    nr = getValue("nr"),
                    mi = getValue("mi"),
                    ci = getValue("ci"),
                    mb = getValue("mb"),
                    cb = getValue("cb"),
                )
            }
        }
    }
}
