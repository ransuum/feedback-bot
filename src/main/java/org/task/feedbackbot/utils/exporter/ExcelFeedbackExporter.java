package org.task.feedbackbot.utils.exporter;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.task.feedbackbot.exception.ExporterException;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.models.enums.ExportFormat;

import java.io.OutputStream;
import java.util.List;

@Component
public final class ExcelFeedbackExporter implements FeedbackExporter {

    @Override
    public void export(List<FeedbackAnalysisDto> feedbacks, OutputStream os, HttpServletResponse response) {
        try (var workbook = new XSSFWorkbook()) {
            final Sheet sheet = workbook.createSheet("Feedbacks");

            String[] columns = {"ID", "Branch", "Message", "Sentiment", "CriticalityLevel", "Category", "Solution"};
            final CellStyle headerStyle = workbook.createCellStyle();
            final Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + setExportName(getFormat()));

            final Row header = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (FeedbackAnalysisDto f : feedbacks) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(f.id());
                row.createCell(1).setCellValue(f.branch());
                row.createCell(2).setCellValue(f.message());
                row.createCell(3).setCellValue(f.sentiment().toString());
                row.createCell(4).setCellValue(f.criticalityLevel().getDisplayName());
                row.createCell(5).setCellValue(f.category());
                row.createCell(6).setCellValue(f.solution());
            }

            workbook.write(os);
            os.flush();
        } catch (Exception e) {
            throw new ExporterException("Excel export exception: " + e.getMessage());
        }
    }

    @Override
    public ExportFormat getFormat() {
        return ExportFormat.EXCEL;
    }
}
