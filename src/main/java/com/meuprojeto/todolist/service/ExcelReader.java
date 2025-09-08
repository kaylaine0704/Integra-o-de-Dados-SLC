package com.meuprojeto.todolist.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.IOUtils;

import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    public static List<String[]> readSLC(String path) throws Exception {
        List<String[]> rows = new ArrayList<>();

        if (path.toLowerCase().endsWith(".csv")) {
            return readSLCCSV(path);
        }

        FileInputStream fis = new FileInputStream(path);
        Workbook wb = loadWorkbook(path);
        Sheet sheet = getSheetByName(wb, "geral"); // aba "geral"

        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // pulando header
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String[] data = new String[20]; // assumindo até coluna T
            for (int j = 0; j < 20; j++) {
                Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                data[j] = getCellValueAsString(cell);
            }
            rows.add(data);
        }

        wb.close();
        return rows;
    }

    public static double getSaxtonValue(String path, double clay, double sand, int columnIndex) throws Exception {
        FileInputStream fis = new FileInputStream(path);
        Workbook wb = loadWorkbook(path);
        Sheet sheet = wb.getSheetAt(0);

        // Assumindo que clay vai para coluna B (1), sand para coluna I (8), linha 5 (4)
        Row row5 = sheet.getRow(4);
        if (row5 != null) {
            row5.getCell(1).setCellValue(clay);
            row5.getCell(8).setCellValue(sand);
            // Retorna o valor da coluna especificada (K=10, L=11, M=12)
            double value = getCellValueAsDouble(row5.getCell(columnIndex));
            wb.close();
            return value;
        }

        wb.close();
        return 0.0;
    }

    private static Workbook loadWorkbook(String path) throws Exception {
        FileInputStream fis = new FileInputStream(path);

        // Configurar limite maior para arquivos Excel grandes
        IOUtils.setByteArrayMaxOverride(200000000); // 200MB

        if (path.endsWith(".xlsx")) {
            return new XSSFWorkbook(fis);
        } else if (path.endsWith(".xls")) {
            return new HSSFWorkbook(fis);
        } else {
            throw new IllegalArgumentException("Formato não suportado: " + path);
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }

    private static double getCellValueAsDouble(Cell cell) {
        if (cell == null) return 0.0;
        return cell.getNumericCellValue();
    }

    private static List<String[]> readSLCCSV(String path) throws Exception {
        List<String[]> rows = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        boolean firstLine = true;

        while ((line = br.readLine()) != null) {
            if (firstLine) {
                firstLine = false; // pular header
                continue;
            }

            String[] data = line.split(","); // assumindo separador por vírgula
            // Garantir que temos pelo menos 20 colunas
            String[] paddedData = new String[20];
            for (int i = 0; i < 20; i++) {
                if (i < data.length) {
                    paddedData[i] = data[i].trim();
                } else {
                    paddedData[i] = "";
                }
            }
            rows.add(paddedData);
        }

        br.close();
        return rows;
    }

    private static Sheet getSheetByName(Workbook workbook, String preferredName) {
        // Tentar o nome preferido primeiro
        Sheet sheet = workbook.getSheet(preferredName);
        if (sheet != null) {
            return sheet;
        }

        // Tentar nomes alternativos comuns
        String[] alternativeNames = {"Planilha1", "Sheet1", "Dados", "data", "slc", "SLC"};
        for (String name : alternativeNames) {
            sheet = workbook.getSheet(name);
            if (sheet != null) {
                System.out.println("⚠️  Aba '" + preferredName + "' não encontrada. Usando aba '" + name + "' como alternativa.");
                return sheet;
            }
        }

        // Se nenhum nome funcionar, usar a primeira aba disponível
        if (workbook.getNumberOfSheets() > 0) {
            sheet = workbook.getSheetAt(0);
            System.out.println("⚠️  Nenhuma aba conhecida encontrada. Usando primeira aba disponível: '" + workbook.getSheetName(0) + "'");
            return sheet;
        }

        throw new IllegalArgumentException("Nenhuma aba encontrada no arquivo Excel");
    }
}
