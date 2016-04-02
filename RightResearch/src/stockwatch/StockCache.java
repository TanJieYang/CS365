/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stockwatch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author loke.k
 */
public class StockCache {
    

    public static boolean Retrieve(ArrayList<StockWatch> returnList, String dir)
    {
        if(returnList == null)
            return false;
        
        Path path = Paths.get(dir);
        BufferedReader bufferedreader = null;
        try {
            bufferedreader = Files.newBufferedReader(path);
        } catch (IOException ex) {
            Logger.getLogger(StockCache.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        try {
            //assuming returnList is already initialized...
            while(bufferedreader.ready())
            {
                String[] strings = bufferedreader.readLine().split(",");
                StockWatch stockwatch = new StockWatch();
                stockwatch.SetCompanyName(strings[0]);
                stockwatch.SetStart(strings[1]);
                stockwatch.SetEnd(strings[2]);
                returnList.add(stockwatch);
            }
        } catch (IOException ex) {
            Logger.getLogger(StockCache.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public static boolean Save(ArrayList<StockWatch> currentList, String fileDir)
    {
        Path path = Paths.get(fileDir);
        BufferedWriter bufferedwriter = null;
        try {
            bufferedwriter = Files.newBufferedWriter(path);
        } catch (IOException ex) {
            Logger.getLogger(StockCache.class.getName()).log(Level.SEVERE, null, ex);
        }
        Iterator<StockWatch> itr = currentList.iterator();
        while(itr.hasNext())
        {
            StockWatch stockwatch = itr.next();
            try {
                bufferedwriter.write(
                        stockwatch.GetCompanyName() + "," +
                        stockwatch.GetStart().toString() + "," +
                        stockwatch.GetEnd().toString()
                );
                bufferedwriter.newLine();
            } catch (IOException ex) {
                Logger.getLogger(StockCache.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            bufferedwriter.close();
        } catch (IOException ex) {
            Logger.getLogger(StockCache.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    
}
