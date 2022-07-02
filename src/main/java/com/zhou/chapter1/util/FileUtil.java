package com.zhou.chapter1.util;

import lombok.experimental.UtilityClass;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.stream.Collectors;

/**
 * @author zhoubing
 * @since 2022/07/03 00:53
 */
@UtilityClass
public class FileUtil {
  public String readFile(File file) throws FileNotFoundException {
    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

    return bufferedReader.lines().collect(Collectors.joining(" "));

  }

}
