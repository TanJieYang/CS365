/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rr;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.PropertyResourceBundle;

/**
 *
 * @author tsh
 */
public class Configuration 
{
    
// ------------------------------------------------------------    
    public static final String jRadioButton1_Text = "Use Pre-Configured URL below : ";
    public static final String jRadioButton2_Text = "Use the URL I typed in below : ";
    public static final String jRadioButton3_Text = "Use Pre-Configured Naming Format : ";
    public static final String jRadioButton4_Text = "Use the file name (on the right) =>";
    public static final String jRadioButton5_Text = "Use Pre-Configured Folder Path : ";
    public static final String jRadioButton6_Text = "Use the Absolute Folder Path (typed in) below : ";
    public static final String jRadioButton7_Text = "Open a File - Save Dialog";
    
// ------------------------------------------------------------    
    
    
    private static final String DEFAULT_FILENAME = "RRConfig.txt";
    
    // Use below path when compiling in Netbeans for debugging ...
    private static final String DEFAULT_RELATIVE_PATH = "\\src\\rr\\";
    
    // Use below path when compiling "release" version
//    private static final String DEFAULT_RELATIVE_PATH = "\\..\\src\\rr\\";
    
    private PropertyResourceBundle prb;
    private String filename;
    private String configFileStringDelimiter;
    
    private StringBuilder rawCompanyInfo;
    
// ------------------------------------------------------------    

    public Configuration ()
    {
        this (DEFAULT_FILENAME);
    }
    
// ------------------------------------------------------------    

    public Configuration (String filename)
    {
        prb = null;
        this.filename = filename;
        configFileStringDelimiter = ", ";
        rawCompanyInfo = new StringBuilder();
        loadConfigData ();
        loadCompanyInfo();
    }
    
    private void loadCompanyInfo()
    {
        FileReader fr = null;
        
        try
        {
            fr = new FileReader("yhf.json");
            
            int b = -1;
            while( (b = fr.read()) != -1)
            {
                char c = (char)b;
                rawCompanyInfo.append(c);
            }
            
            
        }
        catch(Exception e){e.printStackTrace();}

    }
    
    
    public String GetRawCompanySymbolInfo()
    {
        return rawCompanyInfo.toString();
    }
    
// ------------------------------------------------------------    

    private void loadConfigData ()
    {
        
        String programDirectory = System.getProperty("user.dir") + DEFAULT_RELATIVE_PATH;
//        String programDirectory = System.getProperty("user.dir");

        System.out.println ("programDirectory : " + programDirectory);
        
        FileReader fr = null;
        
        try
        {
            fr = new FileReader (programDirectory + filename);
            prb = new PropertyResourceBundle (fr);
            configFileStringDelimiter = prb.getString ("CONFIG_FILE_STRING_DELIMITER");
            
        }
        catch (FileNotFoundException fnfe)
        {
            System.out.println ("FileNotFoundException : " + fnfe.getMessage());
        }
        catch (IOException ioe)
        {           
            System.out.println ("IOException : " + ioe.getMessage());
        }
    }

// ------------------------------------------------------------    

    public String [] getTAB1_SOURCE_EXCHANGE_COMBOBOX_CONTENTS ()
    {
        String [] data = null;
        
        if (prb != null)
        {
            String aLine = prb.getString("TAB1_SOURCE_EXCHANGE_COMBOBOX_CONTENTS");
            data = aLine.split(configFileStringDelimiter);
        }

        for (int i=0; i<data.length; i++)
            System.out.println (i + ") " + data[i]);
        
        return (data);
        
    }

// ------------------------------------------------------------    

    public String [] getTAB1_SOURCE_SGX_TYPESOFINFO_COMBOBOX_CONTENTS ()
    {
        String [] data = null;
        
        if (prb != null)
        {
            String aLine = prb.getString("TAB1_SOURCE_SGX_TYPESOFINFO_COMBOBOX_CONTENTS");
            data = aLine.split(configFileStringDelimiter);
        }

        for (int i=0; i<data.length; i++)
            System.out.println (i + ") " + data[i]);
        
        return (data);
        
    }
    
// ------------------------------------------------------------    

    public String [] getTAB1_SOURCE_KLSE_TYPESOFINFO_COMBOBOX_CONTENTS ()
    {
        String [] data = null;
        
        if (prb != null)
        {
            String aLine = prb.getString("TAB1_SOURCE_KLSE_TYPESOFINFO_COMBOBOX_CONTENTS");
            data = aLine.split(configFileStringDelimiter);
        }

        for (int i=0; i<data.length; i++)
            System.out.println (i + ") " + data[i]);
        
        return (data);
        
    }
    
// ------------------------------------------------------------    

    public String [] getTAB1_SOURCE_NYSE_TYPESOFINFO_COMBOBOX_CONTENTS ()
    {
        String [] data = null;
        
        if (prb != null)
        {
            String aLine = prb.getString("TAB1_SOURCE_NYSE_TYPESOFINFO_COMBOBOX_CONTENTS");
            data = aLine.split(configFileStringDelimiter);
        }

        for (int i=0; i<data.length; i++)
            System.out.println (i + ") " + data[i]);
        
        return (data);
        
    }
        
// ------------------------------------------------------------    
    
    public String [] getTAB1_SOURCE_SGX_URL ()
    {
        if (prb == null)
            return (null);
        
        Enumeration<String> keys = prb.getKeys();

        ArrayList<String> dataArray = new ArrayList<String> ();
        while (keys.hasMoreElements())
        {
            String aKey = keys.nextElement();
            if (aKey.startsWith("TAB1_SOURCE_SGX_URL"))
            {
                dataArray.add (prb.getString (aKey));
            }
        }
        
        String [] data = new String [dataArray.size()];
        
        for (int i=0; i<data.length; i++)
            data [i] = dataArray.get (i);
  
        return (data);
        
    }
    
// ------------------------------------------------------------    

    public String [] getTAB1_SOURCE_KLSE_URL ()
    {
        if (prb == null)
            return (null);
        
        Enumeration<String> keys = prb.getKeys();

        ArrayList<String> dataArray = new ArrayList<String> ();
        while (keys.hasMoreElements())
        {
            String aKey = keys.nextElement();
            if (aKey.startsWith("TAB1_SOURCE_KLSE_URL"))
            {
                dataArray.add (prb.getString (aKey));
            }
        }

        String [] data = new String [dataArray.size()];
        
        for (int i=0; i<data.length; i++)
            data [i] = dataArray.get (i);
  
        return (data);
        
    }

// ------------------------------------------------------------    

    public String [] getTAB1_SOURCE_NYSE_URL ()
    {
        if (prb == null)
            return (null);
        
        Enumeration<String> keys = prb.getKeys();

        ArrayList<String> dataArray = new ArrayList<String> ();
        while (keys.hasMoreElements())
        {
            String aKey = keys.nextElement();
            if (aKey.startsWith("TAB1_SOURCE_NYSE_URL"))
            {
                dataArray.add (prb.getString (aKey));
            }
        }      
        
        String [] data = new String [dataArray.size()];
        
        for (int i=0; i<data.length; i++)
            data [i] = dataArray.get (i);
  
        return (data);
        
    }

// ------------------------------------------------------------    

    public String [] getTAB1_DESTINATION_FILEFORMAT_COMBOBOX_CONTENTS ()
    {
        String [] data = null;
        
        if (prb != null)
        {
            String aLine = prb.getString("TAB1_DESTINATION_FILEFORMAT_COMBOBOX_CONTENTS");
            data = aLine.split(configFileStringDelimiter);
        }

        for (int i=0; i<data.length; i++)
            System.out.println (i + ") " + data[i]);
        
        return (data);
    }
    
// ------------------------------------------------------------    
// ------------------------------------------------------------    
// ------------------------------------------------------------    
// ------------------------------------------------------------    
// ------------------------------------------------------------    
// ------------------------------------------------------------    
    
}   // end class Configuration ...
