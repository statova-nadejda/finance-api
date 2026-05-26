package com.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SummaryReportResponse {
    private double income;
    private double expense;
    private double balance;
}
