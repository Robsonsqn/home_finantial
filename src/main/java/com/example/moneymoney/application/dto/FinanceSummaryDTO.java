package com.example.moneymoney.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record FinanceSummaryDTO(
        BigDecimal personalTotal, // Soma das contas pessoais
        BigDecimal houseTotalExpenses, // Soma total das contas da casa
        BigDecimal userContributionPercentage, // A sua % (ex: 0.405)
        BigDecimal userHouseShare, // Quanto você paga da casa (houseTotal * %)
        BigDecimal grandTotal, // personalTotal + userHouseShare
        List<BillResponseDTO> personalBills, // Detalhe (opcional)
        List<BillResponseDTO> houseBills // Detalhe (opcional)
) {
}
