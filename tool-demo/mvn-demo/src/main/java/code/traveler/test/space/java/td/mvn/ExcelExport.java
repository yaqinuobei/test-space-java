package code.traveler.test.space.java.td.mvn;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExport {

    private String rootPath;
    public ExcelExport(String rootPath) {
        this.rootPath = rootPath;
    }
    public void exportExcel(List<MyFileEntity> jarFileList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("jar_list");

        // 创建标题行
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("groupId");
        titleCell = titleRow.createCell(1);
        titleCell.setCellValue("artifactId");
        titleCell = titleRow.createCell(2);
        titleCell.setCellValue("version");
        titleCell = titleRow.createCell(3);
        titleCell.setCellValue("url");
        titleCell = titleRow.createCell(4);
        titleCell.setCellValue("license");


        for (int i = 0; i < jarFileList.size(); i++) {
            MyFileEntity file = jarFileList.get(i);

            Row row = sheet.createRow(i+1);
            Cell cell = row.createCell(0);
            cell.setCellValue(file.getGroupId());
            cell = row.createCell(1);
            cell.setCellValue(file.getArtifactId());
            cell = row.createCell(2);
            cell.setCellValue(file.getVersion());
            cell = row.createCell(3);
            cell.setCellValue("https://mvnrepository.com/artifact/"+file.getGroupId()+"/"+file.getArtifactId()+"/"+file.getVersion());
            cell = row.createCell(4);
            cell.setCellValue(file.getLicense());
        }

        try (FileOutputStream outputStream = new FileOutputStream(this.rootPath+"\\jar_list.xlsx")) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

