@echo off
echo Starting Excel Processor with two SLC files...
echo.
java -Xmx8g -cp "target/classes;target/dependency/*;target/lib/*" com.meuprojeto.todolist.ExcelProcessorMain "C:\Users\KaylaineSantos\Downloads\Dados_Grupo_1_Organizado_V3.xlsx" test_chemistry.csv output.xlsx
echo.
echo Processing complete. Check output.xlsx for results.
pause
