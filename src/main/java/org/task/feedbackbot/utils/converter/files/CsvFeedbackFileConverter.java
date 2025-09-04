package org.task.feedbackbot.utils.converter.files;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;
import org.task.feedbackbot.exception.ExporterException;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.models.enums.ExportFormat;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public final class CsvFeedbackFileConverter implements FeedbackFileConverter {

    @Override
    public void export(List<FeedbackAnalysisDto> feedbacks, OutputStream os, HttpServletResponse response) {
        try (var printer = new CSVPrinter(
                new OutputStreamWriter(setUTF(os), StandardCharsets.UTF_8),
                CSVFormat.DEFAULT.builder()
                        .setHeader("ID", "Branch", "Message", "Sentiment", "CriticalityLevel", "Category", "Solution")
                        .get())) {

            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + setExportName(getFormat()));

            for (FeedbackAnalysisDto f : feedbacks)
                printer.printRecord(
                        f.id(),
                        f.branch(),
                        f.message(),
                        f.sentiment().getDisplayName(),
                        f.criticalityLevel().getDisplayName(),
                        f.category(),
                        f.solution()
                );

            os.flush();
        } catch (Exception e) {
            throw new ExporterException("Cannot parse into csv: " + e.getMessage());
        }
    }

    @Override
    public ExportFormat getFormat() {
        return ExportFormat.CSV;
    }
}
