package com.meuprojeto.todolist.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class SaxtonReader {

    public static double getFieldCapacity(String saxtonPath, double clay, double sand) throws IOException {
        return getSaxtonValue(saxtonPath, clay, sand, 10); // Coluna K (índice 10)
    }

    public static double getWiltingPoint(String saxtonPath, double clay, double sand) throws IOException {
        return getSaxtonValue(saxtonPath, clay, sand, 11); // Coluna L (índice 11)
    }

    public static double getKsat(String saxtonPath, double clay, double sand) throws IOException {
        return getSaxtonValue(saxtonPath, clay, sand, 12); // Coluna M (índice 12)
    }

    private static double getSaxtonValue(String filePath, double clay, double sand, int columnIndex) throws IOException {
        if (filePath.toLowerCase().endsWith(".csv")) {
            return getSaxtonValueFromCSV(filePath, clay, sand, columnIndex);
        }

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = loadWorkbook(filePath)) {

            Sheet sheet = workbook.getSheetAt(0); // Primeira aba

            // Linha 5 (índice 4)
            Row row5 = sheet.getRow(4);
            if (row5 == null) {
                row5 = sheet.createRow(4);
            }

            // Definir valores de argila (coluna B - índice 1) e areia (coluna I - índice 8)
            Cell clayCell = row5.getCell(1);
            if (clayCell == null) clayCell = row5.createCell(1);
            clayCell.setCellValue(clay);

            Cell sandCell = row5.getCell(8);
            if (sandCell == null) sandCell = row5.createCell(8);
            sandCell.setCellValue(sand);

            // Ler o valor calculado da coluna especificada
            Cell resultCell = row5.getCell(columnIndex);
            if (resultCell != null) {
                return resultCell.getNumericCellValue();
            }

            return 0.0;
        }
    }

    private static Workbook loadWorkbook(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        if (path.toLowerCase().endsWith(".xlsx")) {
            return new XSSFWorkbook(fis);
        } else if (path.toLowerCase().endsWith(".xls")) {
            return new HSSFWorkbook(fis);
        } else if (path.toLowerCase().endsWith(".csv")) {
            // Para CSV, criamos um workbook temporário simples
            // Como não podemos executar fórmulas do Excel em CSV,
            // retornamos valores padrão ou calculados manualmente
            throw new IllegalArgumentException("Saxton CSV não suportado - use arquivo Excel (.xlsx ou .xls)");
        } else {
            throw new IllegalArgumentException("Formato não suportado: " + path);
        }
    }

    private static double getSaxtonValueFromCSV(String filePath, double clay, double sand, int columnIndex) throws IOException {
        // Para CSV, fazemos uma busca simples pelos valores pré-calculados
        // columnIndex: 10 = FieldCapacity, 11 = WiltingPoint, 12 = Ksat
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // pular header
            if (line == null) return 0.0;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    try {
                        double csvClay = Double.parseDouble(parts[0].trim());
                        double csvSand = Double.parseDouble(parts[1].trim());

                        // Verificar se os valores de clay e sand correspondem (com tolerância)
                        if (Math.abs(csvClay - clay) < 0.1 && Math.abs(csvSand - sand) < 0.1) {
                            // Retornar o valor da coluna apropriada
                            if (columnIndex == 10 && parts.length > 2) { // FieldCapacity
                                return Double.parseDouble(parts[2].trim());
                            } else if (columnIndex == 11 && parts.length > 3) { // WiltingPoint
                                return Double.parseDouble(parts[3].trim());
                            } else if (columnIndex == 12 && parts.length > 4) { // Ksat
                                return Double.parseDouble(parts[4].trim());
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar linhas com valores inválidos
                        continue;
                    }
                }
            }
        }

        // Se não encontrou correspondência exata, retorna valor padrão
        return 0.0;
    }
}
