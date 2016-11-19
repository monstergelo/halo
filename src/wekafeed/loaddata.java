package wekafeed;

import weka.core.Instances;
import weka.core.DenseInstance;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.*;
import java.io.*;


public class loaddata {
   
    public static String fileName;
    public static Instances train_data;
    
    public loaddata(String _filename){  
        fileName=_filename;
        
        try {
				BufferedReader reader = new BufferedReader(new FileReader(fileName));
				
				// membaca file dengan format ARFF
				ArffReader arff = new ArffReader(reader);
                                
				// menyimpan isi file (bagian training data) dalam variabel trainData
				train_data = arff.getData();
                                
                                train_data.setClassIndex(train_data.numAttributes()-1);
                                
				reader.close();
		
		} catch (IOException e) {
			e.printStackTrace();
				// kasus jika pembacaan file gagal
				System.out.println("[FAIL] Gagal mengakses dataset: " + fileName + "\n");
				System.out.println("--------------------------------------------------------------------" + "\n");
		}
    }
    
    public static void main(String[] args) {
    
    loaddata load = new loaddata("D:/AI/halo/src/wekafeed/breast-cancer.arff");
    System.out.println("Banyak atribut" + (train_data.numAttributes()-1));
    System.out.println("Banyak kelas" + train_data.numClasses());
  }
    
}
