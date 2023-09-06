package com.zfoo.tank.single.boot.export;

import com.zfoo.storage.util.ExportUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

/**
 * @author sun
 */
@Ignore
public class ExportExcelTest {

    @Test
    public void exportExcel2Csv() throws IOException {
        var inputDir = "D:\\Project\\tank-game-server\\common\\src\\main\\resources\\excel";
        var outputDir = "D:\\";
        ExportUtils.excel2csv(inputDir, outputDir);
    }

}
