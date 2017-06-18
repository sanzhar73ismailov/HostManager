/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author sanzhar.ismailov
 */
public class TestReadFileResutsDevidedtxt {
    public static void main(String[] args) {
        met();
    }
    public static void main1(String[] args) throws FileNotFoundException, IOException {
        String file = "ResutsDevided.txt";
        File f = new File(file);
        FileReader reader = new FileReader(f);
        BufferedReader bReader = new BufferedReader(reader);
        String line = null;
        int i = 0;
        List<Integer> list = new ArrayList<>();
        while((line = bReader.readLine()) != null){
            i++;
//            System.out.println("line = " + line);
            int code = Integer.parseInt(line.substring(0,3).trim());
            //System.out.println(code);
            list.add(code);
        }
        
        Collections.sort(list);
        for (Integer list1 : list) {
            System.out.println(list1);
        }
        
        
    }
    public static void met(){
        String str = "  1 6.35   2 4.69   3 14.1   4 42.9   5 91.5   6 30.0   7 32.8  51 30.1  72 27.3   8 13.7   9 2.51  10  234  11  8.9  20 53.3  21 28.4  22  8.5  23  6.1  24  0.3  25  3.4  28  2.2  14 3.38  15 1.80  16 0.54  17 0.39  18 0.02  19 0.21  29 0.14  50 6.09  52 6.49 19161.31 19265.44  40  +++  79    +  42    +  12 56.8  13 0.21  26 2.29  27 -3.3  73 3.10  83 34.7  84 17.8  53 6.49  54 53.5  55 28.2  56  8.6  57  6.1  58  0.3  59  3.4  60 3.47  61 1.83  62 0.55  63 0.40  64 0.02  65 0.22 ";
        char [] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char b = arr[i];
            System.out.print(b);
            if((i+1)%9 == 0){
                System.out.println("");
            }
            
        }
    }

}
