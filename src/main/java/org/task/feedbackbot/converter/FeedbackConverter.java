package org.task.feedbackbot.converter;

import jakarta.servlet.http.HttpServletResponse;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.models.enums.ExportFormat;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public sealed interface FeedbackConverter permits CsvFeedbackConverter, ExcelFeedbackConverter {
    void export(List<FeedbackAnalysisDto> feedbacks, OutputStream os, HttpServletResponse response);

    ExportFormat getFormat();

    default String setExportName(ExportFormat exportFormat) {
        return "feedbacks_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + exportFormat.getFormat();
    }

    default OutputStream setUTF(OutputStream os) throws IOException {
        os.write(0xEF);
        os.write(0xBB);
        os.write(0xBF);
        return os;
    }

}
