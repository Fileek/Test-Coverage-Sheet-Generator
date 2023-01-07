package entity

import com.gitlab.mvysny.konsumexml.Konsumer

data class SessionInfo(
    val id: String,
    val start: String,
    val dump: String,
) {
    companion object {
        fun xml(k: Konsumer): SessionInfo {
            k.checkCurrent("sessioninfo")
            return with(k.attributes) {
                SessionInfo(
                    id = getValue("id"),
                    start = getValue("start"),
                    dump = getValue("dump"),
                )
            }
        }
    }
}
