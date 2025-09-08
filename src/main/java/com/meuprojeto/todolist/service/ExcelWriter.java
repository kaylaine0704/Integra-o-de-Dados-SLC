package com.meuprojeto.todolist.service;

import com.meuprojeto.todolist.model.SoilRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

public class ExcelWriter {

    public static void writeSoilData(List<SoilRecord> records, String outputPath) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SoilData");

        // Criar header
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "soilDataCode", "soilDataName", "upperDepth", "lowerDepth", "bulkDensity",
            "fieldCapacity", "wiltingPoint", "evapCoeff", "fractionOfRoots", "sandFraction",
            "clayFraction", "orgMatterFraction", "deltaMin", "ksat", "ph", "attributes"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Preencher dados
        int rowIndex = 1;
        for (SoilRecord record : records) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(record.getSoilDataCode());
            row.createCell(1).setCellValue(record.getSoilDataName());
            row.createCell(2).setCellValue(record.getUpperDepth());
            row.createCell(3).setCellValue(record.getLowerDepth());
            row.createCell(4).setCellValue(record.getBulkDensity());
            row.createCell(5).setCellValue(record.getFieldCapacity());
            row.createCell(6).setCellValue(record.getWiltingPoint());
            row.createCell(7).setCellValue(record.getEvapCoeff());
            row.createCell(8).setCellValue(record.getFractionOfRoots());
            row.createCell(9).setCellValue(record.getSandFraction());
            row.createCell(10).setCellValue(record.getClayFraction());
            row.createCell(11).setCellValue(record.getOrgMatterFraction());
            row.createCell(12).setCellValue(record.getDeltaMin());
            row.createCell(13).setCellValue(record.getKsat());
            row.createCell(14).setCellValue(record.getPh());
            row.createCell(15).setCellValue(record.getAttributes());
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Salvar arquivo
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            workbook.write(fos);
        }

        workbook.close();
        System.out.println("Arquivo Excel gerado com sucesso: " + outputPath);
    }
}
