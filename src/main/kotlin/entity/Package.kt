package entity

import com.gitlab.mvysny.konsumexml.Konsumer

data class Package(
    val name: String,
    val classes: List<Class>,
    val sourceFiles: List<SourceFile>,
    val counters: List<Counter>,
) {
    companion object {
        fun xml(k: Konsumer): Package {
            k.checkCurrent("package")
            return Package(
                name = k.attributes.getValue("name"),
                classes = k.children("class") { Class.xml(this) },
                sourceFiles = k.children("sourcefile") { SourceFile.xml(this) },
                counters = k.children("counter") { Counter.xml(this) },
            )
        }
    }
}
