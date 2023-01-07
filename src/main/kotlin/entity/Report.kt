package entity

import com.gitlab.mvysny.konsumexml.Konsumer

data class Report(
    val name: String,
    val sessionInfo: List<SessionInfo>,
    val packages: List<Package>,
    val counter: List<Counter>,
) {
    companion object {
        fun xml(k: Konsumer): Report {
            k.checkCurrent("report")
            return Report(
                name = k.attributes.getValue("name"),
                sessionInfo = k.children("sessioninfo") { SessionInfo.xml(this) },
                packages = k.children("package") { Package.xml(this) },
                counter = k.children("counter") { Counter.xml(this) },
            )
        }
    }
}
