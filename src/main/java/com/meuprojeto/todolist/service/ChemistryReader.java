package com.meuprojeto.todolist.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ChemistryReader {

    public static double getPH(String chemistryPath, String fazenda, String talhao) throws IOException {
        if (chemistryPath.toLowerCase().endsWith(".csv")) {
            return getPHFromCSV(chemistryPath, fazenda, talhao);
        }

        try (FileInputStream fis = new FileInputStream(chemistryPath);
             Workbook workbook = loadWorkbook(chemistryPath)) {

            Sheet sheet = getSheetByName(workbook, "geral"); // Tentar diferentes nomes de aba
            double phValue = -1;
            int lastYear = -1;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Pular cabeçalho

                Cell fazendaCell = row.getCell(2); // Coluna C
                Cell talhaoCell = row.getCell(3); // Coluna D
                Cell yearCell = row.getCell(7); // Coluna H - Ano
                Cell phCell = row.getCell(14); // Coluna O - pH

                if (fazendaCell == null || talhaoCell == null || yearCell == null || phCell == null) continue;

                String rowFazenda = getCellValueAsString(fazendaCell);
                String rowTalhao = getCellValueAsString(talhaoCell);

                if (rowFazenda.equalsIgnoreCase(fazenda) && rowTalhao.equalsIgnoreCase(talhao)) {
                    int year = (int) getCellValueAsDouble(yearCell);
                    if (year > lastYear) {
                        lastYear = year;
                        phValue = getCellValueAsDouble(phCell);
                    }
                }
            }
            return phValue;
        }
    }

    public static String getChemistryAttributes(String chemistryPath, String fazenda, String talhao) throws IOException {
        if (chemistryPath.toLowerCase().endsWith(".csv")) {
            return getChemistryAttributesFromCSV(chemistryPath, fazenda, talhao);
        }

        try (FileInputStream fis = new FileInputStream(chemistryPath);
             Workbook workbook = loadWorkbook(chemistryPath)) {

            Sheet sheet = getSheetByName(workbook, "geral");
            Map<String, String> attributes = new HashMap<>();
            int lastYear = -1;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Pular cabeçalho

                Cell fazendaCell = row.getCell(2);
                Cell talhaoCell = row.getCell(3);
                Cell yearCell = row.getCell(7);

                if (fazendaCell == null || talhaoCell == null || yearCell == null) continue;

                String rowFazenda = getCellValueAsString(fazendaCell);
                String rowTalhao = getCellValueAsString(talhaoCell);

                if (rowFazenda.equalsIgnoreCase(fazenda) && rowTalhao.equalsIgnoreCase(talhao)) {
                    int year = (int) getCellValueAsDouble(yearCell);
                    if (year >= lastYear) {
                        lastYear = year;

                        // Coletar todos os atributos químicos conforme especificações
                        attributes.put("pH-CaCl2", getCellValueAsString(row.getCell(15)));
                        attributes.put("Ph-Água", getCellValueAsString(row.getCell(16)));
                        attributes.put("SMP", getCellValueAsString(row.getCell(17)));
                        attributes.put("H+Al", getCellValueAsString(row.getCell(18)));
                        attributes.put("Al", getCellValueAsString(row.getCell(19)));
                        attributes.put("Ca", getCellValueAsString(row.getCell(20)));
                        attributes.put("Mg", getCellValueAsString(row.getCell(21)));
                        attributes.put("K", getCellValueAsString(row.getCell(22)));
                        attributes.put("P-Mehlich", getCellValueAsString(row.getCell(23)));
                        attributes.put("P-Resina", getCellValueAsString(row.getCell(24)));
                        attributes.put("MO", getCellValueAsString(row.getCell(25)));
                        attributes.put("C", getCellValueAsString(row.getCell(26)));
                        attributes.put("S", getCellValueAsString(row.getCell(27)));
                        attributes.put("Na", getCellValueAsString(row.getCell(28)));
                        attributes.put("B", getCellValueAsString(row.getCell(29)));
                        attributes.put("Fe", getCellValueAsString(row.getCell(30)));
                        attributes.put("Mn", getCellValueAsString(row.getCell(31)));
                        attributes.put("Cu", getCellValueAsString(row.getCell(32)));
                        attributes.put("Zn", getCellValueAsString(row.getCell(33)));
                        attributes.put("Soma de Bases", getCellValueAsString(row.getCell(34)));
                        attributes.put("CTC", getCellValueAsString(row.getCell(35)));
                        attributes.put("V", getCellValueAsString(row.getCell(36)));
                        attributes.put("Relação Ca/Mg", getCellValueAsString(row.getCell(37)));
                        attributes.put("Relação (Ca+Mg)/K", getCellValueAsString(row.getCell(38)));
                        attributes.put("K na CTC", getCellValueAsString(row.getCell(39)));
                        attributes.put("Ca na CTC", getCellValueAsString(row.getCell(40)));
                        attributes.put("Mg na CTC", getCellValueAsString(row.getCell(41)));
                        attributes.put("Al na CTC", getCellValueAsString(row.getCell(42)));
                    }
                }
            }

            // Monta string única no formato atributo=valor; atributo=valor;
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    sb.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
                }
            }

            return sb.toString().trim();
        }
    }

    private static Workbook loadWorkbook(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
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

    private static Sheet getSheetByName(Workbook workbook, String preferredName) {
        // Tentar o nome preferido primeiro
        Sheet sheet = workbook.getSheet(preferredName);
        if (sheet != null) {
            return sheet;
        }

        // Tentar nomes alternativos comuns
        String[] alternativeNames = {"Planilha1", "Sheet1", "Dados", "data", "chemistry", "quimica"};
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

    private static double getPHFromCSV(String chemistryPath, String fazenda, String talhao) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(chemistryPath))) {
            String line = br.readLine(); // pular header
            double phValue = -1;
            int lastYear = -1;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 15) { // garantir que temos pelo menos 15 colunas (até pH)
                    try {
                        String rowFazenda = parts[2].trim(); // Coluna C
                        String rowTalhao = parts[3].trim(); // Coluna D
                        int year = Integer.parseInt(parts[7].trim()); // Coluna H - Ano
                        double ph = Double.parseDouble(parts[14].trim()); // Coluna O - pH

                        if (rowFazenda.equalsIgnoreCase(fazenda) && rowTalhao.equalsIgnoreCase(talhao)) {
                            if (year > lastYear) {
                                lastYear = year;
                                phValue = ph;
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar linhas com valores inválidos
                        continue;
                    }
                }
            }
            return phValue;
        }
    }

    private static String getChemistryAttributesFromCSV(String chemistryPath, String fazenda, String talhao) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(chemistryPath))) {
            String line = br.readLine(); // pular header
            Map<String, String> attributes = new HashMap<>();
            int lastYear = -1;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 16) { // garantir que temos pelo menos 16 colunas
                    try {
                        String rowFazenda = parts[2].trim(); // Coluna C
                        String rowTalhao = parts[3].trim(); // Coluna D
                        int year = Integer.parseInt(parts[7].trim()); // Coluna H - Ano

                        if (rowFazenda.equalsIgnoreCase(fazenda) && rowTalhao.equalsIgnoreCase(talhao)) {
                            if (year >= lastYear) {
                                lastYear = year;

                                // Coletar atributos químicos (colunas P até última disponível)
                                if (parts.length > 15) attributes.put("pH-CaCl2", parts[15].trim());
                                if (parts.length > 16) attributes.put("Ph-Água", parts[16].trim());
                                if (parts.length > 17) attributes.put("SMP", parts[17].trim());
                                if (parts.length > 18) attributes.put("H+Al", parts[18].trim());
                                if (parts.length > 19) attributes.put("Al", parts[19].trim());
                                if (parts.length > 20) attributes.put("Ca", parts[20].trim());
                                if (parts.length > 21) attributes.put("Mg", parts[21].trim());
                                if (parts.length > 22) attributes.put("K", parts[22].trim());
                                if (parts.length > 23) attributes.put("P-Mehlich", parts[23].trim());
                                if (parts.length > 24) attributes.put("P-Resina", parts[24].trim());
                                if (parts.length > 25) attributes.put("MO", parts[25].trim());
                                if (parts.length > 26) attributes.put("C", parts[26].trim());
                                if (parts.length > 27) attributes.put("S", parts[27].trim());
                                if (parts.length > 28) attributes.put("Na", parts[28].trim());
                                if (parts.length > 29) attributes.put("B", parts[29].trim());
                                if (parts.length > 30) attributes.put("Fe", parts[30].trim());
                                if (parts.length > 31) attributes.put("Mn", parts[31].trim());
                                if (parts.length > 32) attributes.put("Cu", parts[32].trim());
                                if (parts.length > 33) attributes.put("Zn", parts[33].trim());
                                if (parts.length > 34) attributes.put("Soma de Bases", parts[34].trim());
                                if (parts.length > 35) attributes.put("CTC", parts[35].trim());
                                if (parts.length > 36) attributes.put("V", parts[36].trim());
                                if (parts.length > 37) attributes.put("Relação Ca/Mg", parts[37].trim());
                                if (parts.length > 38) attributes.put("Relação (Ca+Mg)/K", parts[38].trim());
                                if (parts.length > 39) attributes.put("K na CTC", parts[39].trim());
                                if (parts.length > 40) attributes.put("Ca na CTC", parts[40].trim());
                                if (parts.length > 41) attributes.put("Mg na CTC", parts[41].trim());
                                if (parts.length > 42) attributes.put("Al na CTC", parts[42].trim());
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar linhas com valores inválidos
                        continue;
                    }
                }
            }

            // Monta string única no formato atributo=valor; atributo=valor;
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    sb.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
                }
            }

            return sb.toString().trim();
        }
    }
}
