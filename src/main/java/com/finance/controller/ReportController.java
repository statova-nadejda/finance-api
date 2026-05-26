package com.finance.controller;

import com.finance.dto.SummaryReportResponse;
import com.finance.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/summary")
    public ResponseEntity<SummaryReportResponse> getSummary(@RequestParam Long userId) {
        return ResponseEntity.ok(reportService.getSummary(userId));
    }
}
