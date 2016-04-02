package eac;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jianhui.quek
 */

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import jxl.*; 
import jxl.write.*;

import java.net.URL;
import java.io.*;
import java.net.HttpURLConnection;



public class AttributeHolder 
{
    ArrayList<String> m_Attrib;
    HashMap<String, ArrayList<String>> m_Elem;
    int m_MaxElem;
    
    public AttributeHolder()
    {
        m_Attrib = new ArrayList<String>();
        m_Elem = new HashMap<String, ArrayList<String>>();
        m_MaxElem = 0;
    }
    
    public int GetMaxElem(){return m_MaxElem;}
    
    public void AddAttribute(String str)
    {
        m_Attrib.add(str);
        m_Elem.put(str, new ArrayList<String>());
    }
    
    public boolean AddElementToAttribute(String attribute, String data)
    {
        if(m_Elem.containsKey(attribute))
        {
            m_Elem.get(attribute).add(data);
            int len = m_Elem.get(attribute).size();
            if(len > m_MaxElem)
                m_MaxElem = len;
            
            return true;
        }
        return false;
    }
    
    public ArrayList<String> GetElements(String attribute)
    {
        if(m_Elem.containsKey(attribute))
        {
            return m_Elem.get(attribute);
        }
        
        return null;
    }
    
    public boolean InitJSONArray (String jsonData, String jsonArrayName)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        try
        {
            //JSONParser json_parser = new JSONParser();
            //JSONObject m_obj = (JSONObject)json_parser.parse(jsonData);
            //JSONArray keyArray = (JSONArray)m_obj.get(jsonArrayName);
            JsonNode node = objectMapper.readTree(jsonData).get(jsonArrayName);
            if(node.isArray())
            {
                
                for(int i = 0 ; i < node.size()-1 ; i++) 
                {
                    
                   JsonNode tmp = node.get(i);
                   for(String attrib : m_Attrib)
                   {

                       //if(tmp.containsKey(attrib))
                       if(tmp.has(attrib))
                       {
   
                           String dataInput = tmp.get(attrib).toString();
                           m_Elem.get(attrib).add(dataInput);
                       }
                       int elemSize = m_Elem.get(attrib).size();
                       if(elemSize > m_MaxElem)
                           m_MaxElem = elemSize;
                   }
                }
            }
            else
            {
                System.out.println("Error! Not Error");
            }
            return true;
        }
        catch (Exception e)
        {
              e.printStackTrace();
              System.out.println("Failed Parsing JSON");
              return false;
        }
    }
    
    private boolean isInteger(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
    private int toInteger(String s)
    {
        try
        {
            return Integer.parseInt(s);
        }
        catch(Exception e)
        {
            return -1;
        }
    }
    
    public String GetData(int headerID, int index){
        String data = m_Elem.get(m_Attrib.get(headerID)).get(index).replaceAll("\"", "").replaceAll("\\/", "");
        return data;
    }
    
    public void OutputCSV (String targetDir, ArrayList<String> header)
    {
        try
        {
            //writing to headers/////////////////////////////////
            FileWriter writer = new FileWriter(targetDir);
            
            for(int i = 0; i < header.size(); ++i)
            {
                String[] items = header.get(i).split("%");
                writer.append(items[0].replace(",", " "));
                
                if(i < header.size()-1)
                {
                    writer.append(',');
                }
            }
            writer.append('\n');
            ///////////////////////////////////////////////////

            for(int i = 0; i < m_MaxElem; ++i)
            {

                for(int j = 0; j < header.size(); ++j)
                {
                    
                    String[] items = header.get(j).split("%");
                    //////////NORMAL CASE/////////////////
                    if(items.length < 2)
                    {
                        if(i < m_Elem.get(m_Attrib.get(j)).size())
                        {
                            //writer.append("\"");
                            writer.append(m_Elem.get(m_Attrib.get(j)).get(i).replace(",", ""));
                            //writer.append("\"");
                        }
                    }
                    
                    ////////////////SPECIAL CASE//////////////////////
                    else
                    {
                        StringBuilder sb = new StringBuilder();
                        for(int k = 1; k < items.length; ++k)
                        {
                            
                            String keyArgs[]= items[k].split("(@)");
                            
                            if(keyArgs.length < 3)
                            {
                                //check if its a valid attribute
                                if(m_Elem.containsKey(keyArgs[0]))
                                {
                                    //sb.append(m_Elem.get(items[k]).get(i));
                                    sb.append(m_Elem.get(keyArgs[0]).get(i).replace(",", ""));
                                }
                                else
                                    sb.append(keyArgs[0].replace(",", ""));
                            }
                            else
                            {
        
                                String delimiter = keyArgs[keyArgs.length-1];

                                int index = toInteger(keyArgs[1]);
                                String target = m_Elem.get(keyArgs[0]).get(i).replaceAll("\"", "");
                                String[] dumpItems = target.split(delimiter);
                                
                                
                                if(index < dumpItems.length)
                                {
                                    if(dumpItems[index].length() < 1)
                                    {
                                        sb.append("null");
                                    }
                                    
                                    sb.append(dumpItems[index].replace(",", ""));
                                }
                                else
                                {
                                    sb.append("null");
                                }
                            }
                            
   
                        }
                        //writer.append("\"");
                        writer.append(sb.toString());
                        //writer.append("\"");
                        
                    }
                    
                    ///////////////////////////////////////////////
                    if(j < m_Attrib.size() - 1)
                    {
                        writer.append(',');
                    }
                    
                }
                writer.append('\n');
            }
            
            writer.flush();
            writer.close();
	
        }
        catch(Exception e)
        {
            
        }
    }
    
    public void OutputTLD (String targetDir, ArrayList<String> header)
    {
        try
        {
            //writing to file
            FileWriter writer = new FileWriter(targetDir);
            
            for(int i = 0; i < header.size(); ++i)
            {
                String[] items = header.get(i).split("%");
                writer.append(items[0]);
                
                if(i < header.size()-1)
                {
                    writer.append('\t');
                }
            }
            writer.append('\n');
            
            for(int i = 0; i < m_MaxElem; ++i)
            {

                for(int j = 0; j < header.size(); ++j)
                {
                    
                    String[] items = header.get(j).split("%");
                    //////////NORMAL CASE/////////////////
                    if(items.length < 2)
                    {
                        if(i < m_Elem.get(m_Attrib.get(j)).size())
                        {
                            //writer.append("\"");
                            writer.append(m_Elem.get(m_Attrib.get(j)).get(i));
                            //writer.append("\"");
                        }
                    }
                    
                    ////////////////SPECIAL CASE//////////////////////
                    else
                    {
                        StringBuilder sb = new StringBuilder();
                        for(int k = 1; k < items.length; ++k)
                        {
                            
                            String keyArgs[]= items[k].split("(@)");
                            
                            if(keyArgs.length < 3)
                            {
                                //check if its a valid attribute
                                if(m_Elem.containsKey(keyArgs[0]))
                                {
                                    //sb.append(m_Elem.get(items[k]).get(i));
                                    sb.append(m_Elem.get(keyArgs[0]).get(i));
                                }
                                else
                                    sb.append(keyArgs[0]);
                            }
                            else
                            {
        
                                String delimiter = keyArgs[keyArgs.length-1];

                                int index = toInteger(keyArgs[1]);
                                String target = m_Elem.get(keyArgs[0]).get(i).replaceAll("\"", "");
                                String[] dumpItems = target.split(delimiter);
                                
                                
                                if(index < dumpItems.length)
                                {
                                    if(dumpItems[index].length() < 1)
                                    {
                                        sb.append("null");
                                    }
                                    
                                    sb.append(dumpItems[index]);
                                }
                                else
                                {
                                    sb.append("null");
                                }
                            }
                            
   
                        }
                        //writer.append("\"");
                        writer.append(sb.toString());
                        //writer.append("\"");
                        
                    }
                    
                    ///////////////////////////////////////////////
                    if(j < m_Attrib.size() - 1)
                    {
                        writer.append('\t');
                    }
                    
                }
                writer.append('\n');
            }
            
            writer.flush();
            writer.close();
        }
        catch(Exception e)
        {
            
        }
    }
    
    public void OutputXLS(String targetDir, ArrayList<String> header) {
        /*****************************************************************************************
        NOTE, I HAVEN'T TESTED THIS SO IT MIGHT NOT WORK!
        http://www.andykhan.com/jexcelapi/tutorial.html
        http://www.javaworld.com/article/2074940/learn-java/java-app-dev-reading-and-writing-excel-spreadsheets.html
        ******************************************************************************************/
        /*
         *writing to file
         *This is for xls file
         */
        
        try 
        {
            WritableWorkbook wworkbook = Workbook.createWorkbook(new File(targetDir));
            WritableSheet wsheet = wworkbook.createSheet("Sheet 1", 0);
            wsheet.setColumnView(0, 30);
            wsheet.setColumnView(1, 30);
            wsheet.setColumnView(2, 30);
            wsheet.setColumnView(3, 30);
            wsheet.setColumnView(4, 30);
            wsheet.setColumnView(5, 30);
            
                WritableFont font = new WritableFont(WritableFont.ARIAL,14);
                font.setColour(Colour.WHITE);
                font.setBoldStyle(WritableFont.BOLD);
                
                WritableCellFormat wcf = new WritableCellFormat(font);
                wcf.setAlignment(Alignment.CENTRE);
                wcf.setBackground(Colour.BLUE);
                wcf.setBorder(Border.ALL, BorderLineStyle.THICK);
                
                Label label_1;
                for(int i = 0; i < header.size(); ++i) 
                {
                  String[] items = header.get(i).split("%");
                  label_1 = new Label(i, 0, items[0]);
                  label_1.setCellFormat(wcf);
                  wsheet.addCell(label_1);
                }
                
                WritableFont font_2 = new WritableFont(WritableFont.ARIAL,10);
                font_2.setBoldStyle(WritableFont.NO_BOLD);

                WritableCellFormat wcf_2 = new WritableCellFormat(font_2);
                wcf_2.setAlignment(Alignment.CENTRE);
                wcf_2.setBorder(Border.ALL, BorderLineStyle.THIN);
                
                Label label_2;
                for(int i = 1; i < m_MaxElem+1; ++i)
                {
                        for(int j = 0; j < header.size(); ++j)
                        {       
                                String[] items = header.get(j).split("%");
                                //////////NORMAL CASE/////////////////
                                if(items.length < 2)
                                {
                                        if(i < m_Elem.get(m_Attrib.get(j)).size()+1)
                                        {
                                                String element = m_Elem.get(m_Attrib.get(j)).get(i-1).replaceAll("\"", "");

                                                label_2 = new Label(j, i, element);
                                                label_2.setCellFormat(wcf_2);
                                                wsheet.addCell(label_2);
                                        }
                                }

                                ////////////////SPECIAL CASE//////////////////////
                                else
                                {
                                        StringBuilder sb = new StringBuilder();
                                        for(int k = 1; k < items.length; ++k)
                                        {

                                                String keyArgs[]= items[k].split("(@)");

                                                if(keyArgs.length < 3)
                                                {
                                                        //check if its a valid attribute
                                                        if(m_Elem.containsKey(keyArgs[0]))
                                                        {
                                                                //sb.append(m_Elem.get(items[k]).get(i));
                                                                sb.append(m_Elem.get(keyArgs[0]).get(i-1));
                                                        }
                                                        else
                                                                sb.append(keyArgs[0]);
                                                }
                                                else
                                                {

                                                        String delimiter = keyArgs[keyArgs.length-1];

                                                        int index = toInteger(keyArgs[1]);
                                                        String target = m_Elem.get(keyArgs[0]).get(i-1).replaceAll("\"", "");
                                                        String[] dumpItems = target.split(delimiter);


                                                        if(index < dumpItems.length)
                                                        {
                                                                if(dumpItems[index].length() < 1)
                                                                {
                                                                        sb.append("null");
                                                                }

                                                                sb.append(dumpItems[index]);
                                                        }
                                                        else
                                                        {
                                                                sb.append("null");
                                                        }
                                                }


                                        }

                                        label_2 = new Label(j, i, sb.toString());
                                        label_2.setCellFormat(wcf_2);
                                        wsheet.addCell(label_2);

                                }		
                        }
                }
                
            wworkbook.write();
            wworkbook.close();
        }
        catch (Exception e) 
        {
            
        }
       
    }
    
    public boolean CreateFolder(String directoryName){
        File theDir = new File(directoryName);
        
        if (!theDir.exists()) {

            try{
                
                boolean result = theDir.mkdir();
                return result;
            } 
            catch(SecurityException se){
                //handle it
                return false;
            }        
        }
        return false;
    }
    
    public void OutputHTML() {
        String link = "http://infopub.sgx.com/Apps?A=Cow_CorporateInformation_Content";
        String join;
        String name;
        URL urlLink;
        PrintWriter outputFile;
        HttpURLConnection con;
        InputStream is;
        BufferedReader br;
        String directoryName = "Corporate Info";
        
        CreateFolder(directoryName);
        
        System.out.println("Current max Element: " + m_MaxElem);
        //m_MaxElem+1
        try {
            for(int i = 1; i < m_MaxElem+1; ++i){
            
                name = GetData(0,i);
                join = GetData(1,i);
                urlLink = new URL(link+join);
                
                String targetDir = directoryName + File.separator + name + ".html";
                //System.out.println("writing-> " + targetDir);
                //outputFile = new PrintWriter("corporateInfo"+i+".html");
                outputFile = new PrintWriter(targetDir);
                con = (HttpURLConnection)urlLink.openConnection();
                
                is = con.getInputStream();

                br = new BufferedReader(new InputStreamReader(is));

                String line = null;


                while ((line = br.readLine()) != null) {
                    //System.out.println(line);
                    outputFile.println(line);
                }
                
                
                br.close();
                outputFile.close();
                con.disconnect();
                
                //System.out.println("success-> " + targetDir);
            }
            
        }
        catch (Exception e) {
            //e.printStackTrace();
              //System.out.println("Failed Parsing HTML");
        }
    }
}
