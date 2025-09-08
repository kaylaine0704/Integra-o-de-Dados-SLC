package com.meuprojeto.todolist;

import com.meuprojeto.todolist.model.SoilRecord;
import com.meuprojeto.todolist.service.ExcelReader;
import com.meuprojeto.todolist.service.ExcelTransformerNew;
import com.meuprojeto.todolist.service.ExcelWriter;

import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExcelProcessorMain {

    public static void main(String[] args) {
        try {
            // Verificar argumentos da linha de comando
            if (args.length < 3) {
                System.out.println("Uso: java ExcelProcessorMain <caminho_slc> <caminho_quimica> <caminho_saida>");
                System.out.println("Exemplo: java ExcelProcessorMain dados_slc.xlsx quimica.xlsx saida.xlsx");
                System.exit(1);
            }

            String slcPath = args[0];
            String chemistryPath = args[1];
            String outputPath = args[2];

            // Verificar se os arquivos de entrada existem
            if (!Files.exists(Paths.get(slcPath))) {
                System.err.println("❌ Arquivo SLC não encontrado: " + slcPath);
                System.exit(1);
            }
            if (!Files.exists(Paths.get(chemistryPath))) {
                System.err.println("❌ Arquivo de química não encontrado: " + chemistryPath);
                // System.exit(1);
            }

            System.out.println("🚀 Iniciando processamento das planilhas...");
            System.out.println("📁 Arquivo SLC: " + slcPath);
            System.out.println("📁 Arquivo Química: " + chemistryPath);
            System.out.println("📁 Arquivo de saída: " + outputPath);

            // 1. Ler dados da planilha SLC
            System.out.println("📖 Lendo dados da planilha SLC...");
            List<String[]> slcData = ExcelReader.readSLC(slcPath);
            System.out.println("✅ Encontrados " + slcData.size() + " registros na SLC");

            // 2. Filtrar dados válidos (remover linhas vazias)
            List<String[]> validData = new ArrayList<>();
            for (String[] row : slcData) {
                if (row.length > 3 && row[2] != null && !row[2].trim().isEmpty()) {
                    validData.add(row);
                }
            }
            System.out.println("📊 Dados válidos após filtragem: " + validData.size());

            // 3. Transformar dados aplicando regras
            System.out.println("🔄 Transformando dados...");
            List<SoilRecord> transformedData = ExcelTransformerNew.transformData(validData, chemistryPath);
            System.out.println("✅ Gerados " + transformedData.size() + " registros transformados");

            // 4. Escrever nova planilha
            System.out.println("💾 Escrevendo nova planilha...");
            ExcelWriter.writeSoilData(transformedData, outputPath);

            System.out.println("🎉 Processamento concluído com sucesso!");
            System.out.println("📁 Arquivo de saída: " + outputPath);

            // Mostrar exemplo dos primeiros registros
            if (!transformedData.isEmpty()) {
                System.out.println("\n📋 Exemplo dos primeiros registros:");
                for (int i = 0; i < Math.min(5, transformedData.size()); i++) {
                    SoilRecord record = transformedData.get(i);
                    System.out.println("Registro " + (i+1) + ": " + record.getSoilDataCode() +
                                     " | Profundidade: " + record.getUpperDepth() + "-" + record.getLowerDepth() +
                                     " | Capacidade: " + String.format("%.2f", record.getFieldCapacity()));
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Erro durante o processamento: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
