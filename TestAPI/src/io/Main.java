package io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File("")));
    }
}
