package com.coderedrobotics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class QuestionLoader {

    public static int MATCHES = 0;
    public static int BATTERIES = 1;

    public static void readCSVFile(String path, String csvSplitBy) {
        BufferedReader read;
        String line;
        try {
            read = new BufferedReader(new FileReader(path));
            while ((line = read.readLine()) != null) {
                String[] data = line.split(">");
                QuestionManager.addQuestion(convertToQuestion(data));
            }
            read.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(QuestionLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QuestionLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static Question convertToQuestion(String[] data) {
        return new Question(data[0], data[1], data[2], Arrays.copyOfRange(data, 3, data.length));
    }
}
