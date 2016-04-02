/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eac;

import java.io.IOException;
import java.util.regex.*;
import java.util.Calendar;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.*; //stream<path>
import java.nio.file.*; //files;paths;path
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
/**
 *
 * @author w.wong
 */
public class RegexConverter {
    //private static finals
    final List<String> REGEXS = Arrays.asList
            ("<Year>", "<Month>", "<Day>", "<Hr>",
            "<Min>", "<WebPageID>", "<Market>", "<Sn>");
    List<String> REPLACES;
    String target;
    String extension;
    
    
    //public methods
    public RegexConverter()
    {
        REPLACES = new ArrayList<>();
    }
    
    public String convertRegex(
            String arg, String webpageID, String market,
            Path absoluteFolderPath, String fileExt
    ) throws IOException
    {
        target = arg;
        extension = fileExt;
        
        Calendar cdr = Calendar.getInstance();
        
        REPLACES.clear();
        
        //Year
        DateFormat df = new SimpleDateFormat("yyyy");
        REPLACES.add(df.format(cdr.getTime()));
        
        //Month
        df = new SimpleDateFormat("MM");
        REPLACES.add(df.format(cdr.getTime()));
        
        //Day
        df = new SimpleDateFormat("dd");
        REPLACES.add(df.format(cdr.getTime()));
        
        //hr
        df = new SimpleDateFormat("HH");
        REPLACES.add(df.format(cdr.getTime()));
        
        //min
        df = new SimpleDateFormat("mm");
        REPLACES.add(df.format(cdr.getTime()));
        
        //MainWindow responsible for providing webpageID and market
        REPLACES.add(webpageID);
        REPLACES.add(market);
        
        //todo: SN
        //MainWindow responsible for providing VALID save folder absolute path
        //and making sure that program has read access to the folder.
        
        // --- Lambda code ---
        /*
        Stream<Path> stream = Files.list(absoluteFolderPath);
        Stream<Path> filtered = stream.filter(p -> {return p.toString().endsWith(fileExt);});
        REPLACES.add(Long.toString(filtered.count() + 1));
        */
        
        // --- non-Lambda code ---
        List<Object> pathList = Files.list(absoluteFolderPath).collect(Collectors.toList());
        Iterator<Object> itr = pathList.iterator();
        int count = 0;
        while(itr.hasNext())
        {
            String elem = itr.next().toString();
            if(elem.endsWith(extension))
                count++;
        }
        REPLACES.add(Long.toString(count + 1));
        
        Iterator<String> REGEXSitr = REGEXS.iterator();
        Iterator<String> REPLACESitr = REPLACES.iterator();
        
        while(REGEXSitr.hasNext() && REPLACESitr.hasNext())
        {
            Object regex = REGEXSitr.next();
            Object replace = REPLACESitr.next();
            Pattern p = Pattern.compile(regex.toString());
            Matcher m = p.matcher(target);
            target = m.replaceAll(replace.toString());
        }
        
        return target;
    }
    
}
