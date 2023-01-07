import java.io.File

fun main(args: Array<String>) {
    val jacocoTestReportParser = JacocoTestReportParser()
    val sheetParser = SheetReportParser
    val files = File("./reports/").walk().filter {
        it.name == "jacocoTestReport.xml" || it.name == "androidTestCoverageReport.xml"
    }
    val reports = files.map { jacocoTestReportParser.parse(it) }

    val filepath = "./jacoco_test_coverage.xlsx"
    reports.forEach { sheetParser.parse(it) }
    sheetParser.saveSheet(filepath)
}