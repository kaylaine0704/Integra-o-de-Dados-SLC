package com.meuprojeto.todolist.service;

import com.meuprojeto.todolist.model.SoilRecord;

import java.util.ArrayList;
import java.util.List;

public class ExcelTransformerNew {

    // Valores fixos conforme especificações
    private static final double[] UPPER_DEPTHS = {0, 5, 15, 30, 60};
    private static final double[] LOWER_DEPTHS = {5, 15, 30, 60, 100};
    private static final double[] EVAP_COEFFS = {0.8, 0.2, 0, 0, 0};
    private static final double[] FRACTION_OF_ROOTS = {0.4, 0.25, 0.3, 0.1, 0.05};
    private static final double[] ORG_MATTER_FRACTIONS = {0.2, 0.2, 0.2, 0.2, 0.1};
    private static final double[] DELTA_MINS = {0.008, 0.008, 0.006, 0.004, 0.002};
    private static final double BULK_DENSITY = 1.23;

    public static List<SoilRecord> transformData(List<String[]> slcData, String chemistryPath) throws Exception {
        List<SoilRecord> records = new ArrayList<>();

        for (String[] row : slcData) {
            // Extrair valores da linha SLC
            String colunaC = row[2]; // Coluna C - Fazenda
            String colunaD = row[3]; // Coluna D - Talhão
            double clayFraction = parseDouble(row[6]); // Coluna G - Argila
            double sandFraction = parseDouble(row[8]); // Coluna I - Areia

            // soilDataCode e soilDataName
            String soilDataCode = colunaC + "_" + colunaD;
            String soilDataName = "Fazenda " + colunaC + " Talhão " + colunaD;

            // Calcular valores usando SaxtonCalculator (substituindo SaxtonReader)
            com.meuprojeto.todolist.service.SaxtonCalculator.SoilProperties props = com.meuprojeto.todolist.service.SaxtonCalculator.calculate(sandFraction * 100, clayFraction * 100);
            double fieldCapacity = props.getFieldCapacity().doubleValue();
            double wiltingPoint = props.getWiltingPoint().doubleValue();
            double ksat = props.getAvailableWater().doubleValue();

            // Fix formatting issue: multiply by 100 to convert fraction to percentage for output
            fieldCapacity = fieldCapacity * 100;
            wiltingPoint = wiltingPoint * 100;
            ksat = ksat * 100;

            // pH (busca via ChemistryReader)
            String[] parts = soilDataCode.split("_");
            String fazenda = parts.length > 0 ? parts[0] : "";
            String talhao = parts.length > 1 ? parts[1] : "";

            double ph = ChemistryReader.getPH(chemistryPath, fazenda, talhao);

            // Atributos completos concatenados via ChemistryReader
            String attributes = ChemistryReader.getChemistryAttributes(chemistryPath, fazenda, talhao);

            // Gerar 5 registros
            for (int i = 0; i < 5; i++) {
                SoilRecord record = new SoilRecord();
                record.setSoilDataCode(soilDataCode);
                record.setSoilDataName(soilDataName);
                record.setUpperDepth((int) UPPER_DEPTHS[i]);
                record.setLowerDepth((int) LOWER_DEPTHS[i]);
                record.setBulkDensity(BULK_DENSITY);
                record.setFieldCapacity(fieldCapacity);
                record.setWiltingPoint(wiltingPoint);
                record.setEvapCoeff(EVAP_COEFFS[i]);
                record.setFractionOfRoots(FRACTION_OF_ROOTS[i]);
                record.setSandFraction(sandFraction);
                record.setClayFraction(clayFraction);
                record.setOrgMatterFraction(ORG_MATTER_FRACTIONS[i]);
                record.setDeltaMin(DELTA_MINS[i]);
                record.setKsat(ksat);
                record.setPh(ph);
                record.setAttributes(attributes);

                records.add(record);
            }
        }

        return records;
    }

    private static double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static double findPhBySigla(List<String[]> data, String sigla) {
        // Implementação simplificada - buscar na coluna O o pH mais recente
        double latestPh = 0.0;
        int latestYear = 0;

        for (String[] row : data) {
            String rowSigla = row[2] + "_" + row[3];
            if (rowSigla.equals(sigla)) {
                int year = (int) parseDouble(row[7]); // Coluna H - Ano
                if (year > latestYear) {
                    latestYear = year;
                    latestPh = parseDouble(row[14]); // Coluna O - pH
                }
            }
        }

        return latestPh;
    }

    private static String buildAttributes(String[] row) {
        // Implementação completa conforme especificações
        List<String> attrs = new ArrayList<>();
        if (row.length > 15) {
            attrs.add("pH-CaCl2:" + getSafeValue(row, 15));
            attrs.add("Ph-Água:" + getSafeValue(row, 16));
            attrs.add("SMP:" + getSafeValue(row, 17));
            attrs.add("H+Al:" + getSafeValue(row, 18));
            attrs.add("Al:" + getSafeValue(row, 19));
            attrs.add("Ca:" + getSafeValue(row, 20));
            attrs.add("Mg:" + getSafeValue(row, 21));
            attrs.add("K:" + getSafeValue(row, 22));
            attrs.add("P-Mehlich:" + getSafeValue(row, 23));
            attrs.add("P-Resina:" + getSafeValue(row, 24));
            attrs.add("MO:" + getSafeValue(row, 25));
            attrs.add("C:" + getSafeValue(row, 26));
            attrs.add("S:" + getSafeValue(row, 27));
            attrs.add("Na:" + getSafeValue(row, 28));
            attrs.add("B:" + getSafeValue(row, 29));
            attrs.add("Fe:" + getSafeValue(row, 30));
            attrs.add("Mn:" + getSafeValue(row, 31));
            attrs.add("Cu:" + getSafeValue(row, 32));
            attrs.add("Zn:" + getSafeValue(row, 33));
            attrs.add("Soma de Bases:" + getSafeValue(row, 34));
            attrs.add("CTC:" + getSafeValue(row, 35));
            attrs.add("V:" + getSafeValue(row, 36));
            attrs.add("Relação Ca/Mg:" + getSafeValue(row, 37));
            attrs.add("Relação (Ca+Mg)/K:" + getSafeValue(row, 38));
            attrs.add("K na CTC:" + getSafeValue(row, 39));
            attrs.add("Ca na CTC:" + getSafeValue(row, 40));
            attrs.add("Mg na CTC:" + getSafeValue(row, 41));
            attrs.add("Al na CTC:" + getSafeValue(row, 42));
        }
        return String.join(";", attrs);
    }

    private static String getSafeValue(String[] row, int index) {
        if (index < row.length && row[index] != null && !row[index].trim().isEmpty()) {
            return row[index].trim();
        }
        return "";
    }
}
