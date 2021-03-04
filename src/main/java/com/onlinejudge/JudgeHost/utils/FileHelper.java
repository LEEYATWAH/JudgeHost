package com.onlinejudge.JudgeHost.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @return $
 * @param
 * @author LeeYatWah
 * @date $ $
 * @description
 */


public class FileHelper {
    public static Boolean isFileIn(String filePath){
        File file = new File(filePath);
        return file.exists();
    }

    public static Boolean isDirectory(String filePath){
        File directory = new File(filePath);
        return directory.isDirectory();
    }

    public static List<String> readFileByLines(String filePath) throws IOException {
        File file = new File(filePath);
        BufferedReader bufferedReader = null;
        bufferedReader = new BufferedReader(new FileReader(file));
        String tempStr;
        List<String> stringList = new ArrayList<>();
        while ((tempStr = bufferedReader.readLine()) != null) {
            stringList.add(tempStr);
        }
        return stringList;
    }

    //压缩某个文件夹，并保存到目标位置
    public static Boolean zipDictionary(String zippedPath, String targetPath) throws InterruptedException, IOException {
        if (!isDirectory(targetPath)) {
            return false;
        } else {
            Runtime runtime = Runtime.getRuntime();
            String[] zipCommand = {
                    "zip",
                    "-j",
                    "-r",
                    zippedPath,
                    targetPath
            };
            Process process = runtime.exec(zipCommand);
            process.waitFor();
        }
        return true;
    }
}
