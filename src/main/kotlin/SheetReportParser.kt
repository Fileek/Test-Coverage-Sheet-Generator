import entity.Counter
import entity.Report
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

object SheetReportParser {
    private var rowCounter = 0
    private val book = XSSFWorkbook()
    private val sheet = book.createSheet()

    init {
        setHeaderRow()
    }

    private fun setHeaderRow() {
        val headerRow = sheet.createRow(rowCounter)

        HeaderCells.values().forEach {
            val cell = headerRow.createCell(it.ordinal).center().bold()
            cell.setCellValue(it.value)
        }
    }

    fun parse(report: Report) {
        val coveragePercent = getCoveragePercent(report.counter.branch())
        if (coveragePercent == 100) return
        var zeroCovered = ""
        var below50Covered = ""
        var below80Covered = ""
        var below100Covered = ""
        val row = createRow(++rowCounter)

        row.getCell(HeaderCells.MODULE_NAME.ordinal).setCellValue(report.name)
        row.getCell(HeaderCells.COVERAGE_PERCENTAGE.ordinal).apply {
            setCellValue(coveragePercent / 100.0)
            cellStyle.apply { dataFormat = book.createDataFormat().getFormat("0%") }
        }

        val notCoveredPackages = report.packages.filter {
            isNotCovered(it.counters)
        }
        notCoveredPackages.forEach {
            val notCoveredClasses = it.sourceFiles.filter { src ->
                isNotCovered(src.counters)
            }
            notCoveredClasses.forEach { src ->
                src.counters.branch()?.let { counter ->
                    val className = src.name.substringBefore('.') + "\n"
                    when (getCoveragePercent(counter)) {
                        0 -> zeroCovered += className
                        in 1..50 -> below50Covered += className
                        in 51..80 -> below80Covered += className
                        else -> below100Covered += className
                    }
                }
            }
        }
        println(report.name)
        row.getCell(HeaderCells.ZERO_COVERAGE.ordinal).setCellValue(zeroCovered.trimEnd())
        row.getCell(HeaderCells.BELOW_50_COVERAGE.ordinal).setCellValue(below50Covered.trimEnd())
        row.getCell(HeaderCells.BELOW_80_COVERAGE.ordinal).setCellValue(below80Covered.trimEnd())
        row.getCell(HeaderCells.BELOW_100_COVERAGE.ordinal).setCellValue(below100Covered.trimEnd())

        row.autoSize(zeroCovered, below50Covered, below80Covered, below100Covered)
        autoSizeColumns()
    }

    private fun Row.autoSize(vararg content: String) {
        val longestString = content.maxBy { it.countLineBreaks() }
        heightInPoints = (longestString.countLineBreaks() * 15f).coerceIn(15f, 409f)
        println("$rowCounter - $heightInPoints - $longestString")
    }

    private fun String.countLineBreaks() = count { ch -> ch == '\n' }

    private fun createRow(index: Int): Row {
        val row = sheet.createRow(index)
        HeaderCells.values().forEach {
            row.createCell(it.ordinal).center()
        }
        return row
    }

    private fun Cell.bold() = apply {
        cellStyle.apply {
            setFont(
                book.createFont().also { font ->
                    font.fontName = "Calibri"
                    font.bold = true
                }
            )
        }
    }

    private fun Cell.center() = apply {
        cellStyle = book.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            wrapText = true
        }
    }

    private fun getCoveragePercent(counter: Counter?): Int {
        return counter?.let {
            val total = it.covered.toInt() + it.missed.toInt()
            it.covered.toInt() * 100 / total
        } ?: 100
    }

    private fun isNotCovered(counters: List<Counter>): Boolean {
        return try {
            getCoveragePercent(counters.branch()) < 100
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun autoSizeColumns() {
        HeaderCells.values().forEach {
            sheet.autoSizeColumn(it.ordinal)
        }
    }

    fun saveSheet(filePath: String) {
        val outputStream = FileOutputStream(filePath)
        outputStream.use {
            book.write(it)
        }
    }

    private enum class HeaderCells(val value: String) {
        MODULE_NAME("module"),
        COVERAGE_PERCENTAGE("coverage"),
        ZERO_COVERAGE("0% classes"),
        BELOW_50_COVERAGE("1-50% classes"),
        BELOW_80_COVERAGE("51-80% classes"),
        BELOW_100_COVERAGE("81-100% classes"),
    }
}

fun List<Counter>.branch() = find { it.type == "BRANCH" }