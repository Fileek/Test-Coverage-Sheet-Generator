import com.gitlab.mvysny.konsumexml.konsumeXml
import entity.Report
import java.io.File

class JacocoTestReportParser {

    fun parse(file: File): Report {
        return file.konsumeXml().use { k ->
            k.child("report") { Report.xml(this) }
        }
    }
}