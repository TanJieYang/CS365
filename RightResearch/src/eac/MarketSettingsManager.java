/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eac;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.json.simple.*;
import org.json.simple.parser.*;
/**
 *
 * @author Kyanern
 */
public class MarketSettingsManager {
    
    //internal private classes
    private class datastruct
    {
        //private members
        private String exch;
        private LinkedHashMap<String, String> info;
        
        //public methods
        datastruct()
        {
            exch = "dummy";
            info = new LinkedHashMap<>(); //String, String
        }
        
        public void exchSet(String arg)
        {
            exch = arg;
        }
        
        public String exchGet()
        {
            return exch;
        }
        
        public void infoAdd(String key, String val)
        {
            info.put(key, val);
        }
        
        public void infoRemove(String key)
        {
            info.remove(key);
        }
        
        public void infoUpdate(String key, String val)
        {
            info.replace(key, val);
        }
        
        public LinkedHashMap<String,String> infoGet()
        {
            return info;
        }
        
        public String infoGetSingle (String key)
        {
            return info.get(key);
        }
        
        public Set<String> infoGetKeysAsSet()
        {
            return info.keySet();
        }
        
        public void clear()
        {
            exch = "dummy";
            info.clear();
        }
    //end of class datastruct
    }
    
    private class JSONWriter
    {
        //public methods
        JSONWriter()
        {
            
        }
        public void writeTo(String pathStr, LinkedHashMap<String,datastruct> dat)
        {
            Path path = Paths.get(pathStr);
            //attempt to create file
            if(Files.exists(path) == false)
            {
             try 
             {
                Files.createFile(path);
             } 
             catch (IOException ex) 
             {
                Logger.getLogger(MarketSettingsManager.class.getName()).log(Level.SEVERE, null, ex);
             }   
            }
            
            //prepare JSON structure
            JSONObject root = new JSONObject();
            JSONArray root2 = new JSONArray();
            //iterate thru per-market settings
            Set<String> datkeyset = dat.keySet();
            Iterator<String> datitr = datkeyset.iterator();
            while(datitr.hasNext())
            {
                //get market
                String datkey = datitr.next();
                datastruct elem = dat.get(datkey);
                //json-simple treats JSONObject as a Map
                //however we want to retain the order of elements here
                Map obj = new LinkedHashMap();
                obj.put("market", elem.exchGet());
                
                //insert the key-value pairs into obj
                Set<String> keyset = elem.infoGetKeysAsSet();
                Iterator<String> keysetitr = keyset.iterator();
                JSONObject jobj = new JSONObject();
                while(keysetitr.hasNext())
                {
                    String key = keysetitr.next();
                    String val = elem.infoGetSingle(key);
                    jobj.put(key, val);
                }
                obj.put("options", jobj);
                
                //insert obj into the JSONArray
                root2.add(obj);
            }
            //insert JSONArray into root JSONobject
            root.put("marketsettings",root2);
            
            //write to file
            String jsontxt = JSONValue.toJSONString(root);
            List<String> lines = Arrays.asList(jsontxt);
            try {
                Files.write(path, lines, Charset.forName("UTF-8"));
            } catch (IOException ex) {
                Logger.getLogger(MarketSettingsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        //end of function
        }
    //end of class JSONWriter
    }
    
    private class JSONReader
    {
        JSONReader()
        {
            
        }
        public void readFrom(String pathStr, LinkedHashMap<String,datastruct> dat)
        {
            //does not error check beyond file existing!
            //only do reading if file exists
            Path path = Paths.get(pathStr);
            if(Files.exists(path) == true)
            {
                //extract JSON text
                Stream<String> stream = null;
                try {
                    stream = Files.lines(path, Charset.forName("UTF-8"));
                } catch (IOException ex) {
                    Logger.getLogger(MarketSettingsManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                Iterator<String> streamitr = stream.iterator();
                String jsontxt = streamitr.next();
                
                //begin parsing JSON text
                JSONParser parser = new JSONParser();
                Object jsonobj = null;
                try {
                     jsonobj = parser.parse(jsontxt);
                } catch (ParseException ex) {
                    Logger.getLogger(MarketSettingsManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                JSONObject root = (JSONObject)jsonobj;
                JSONArray root2 = (JSONArray)root.get("marketsettings");
                for(int i = 0; i < root2.size(); i++)
                {
                    datastruct elem = new datastruct();
                    JSONObject obj = (JSONObject)root2.get(i);
                    elem.exchSet((String)obj.get("market"));
                    JSONObject jobj = (JSONObject)obj.get("options");
                    Set<String> set = jobj.keySet();
                    Iterator<String> setitr = set.iterator();
                    while(setitr.hasNext())
                    {
                        String key = setitr.next();
                        String val = (String)jobj.get(key);
                        elem.infoAdd(key, val);
                    }
                    dat.put(elem.exchGet(),elem);
                }
            }
        //end of function
        }
    //end of class JSONReader
    }
    
    //private members
    private final LinkedHashMap<String, datastruct> dat;
    private final String DEFAULTPATH = "marketdefaultsettings.json";
    private final JSONWriter writer;
    private final JSONReader reader;
    
    //public methods
    MarketSettingsManager()
    {
        dat = new LinkedHashMap<>();
        writer = new JSONWriter();
        reader = new JSONReader();
    }
    
    //private methods
    private void debugGenerateData()
    {
        clearData();
        datastruct tmp = new datastruct();
        tmp.exchSet("SGX");
        tmp.infoAdd("Corporate Action", "http://www.sgx.com/wps/portal/sgxweb/home/company_disclosure/corporate_action");
        tmp.infoAdd("haha", "dummy1");
        tmp.infoAdd("hehe", "dummy2");
        dat.put(tmp.exchGet(), tmp);
        
        tmp = new datastruct();
        tmp.exchSet("KLSE");
        tmp.infoAdd("lol", "dummy1");
        tmp.infoAdd("lolol", "dummy2");
        tmp.infoAdd("lololol", "dummy3");
        tmp.infoAdd("lolololol", "dummy4");
        dat.put(tmp.exchGet(), tmp);
    }
    
    private void clearData()
    {
        dat.clear();
    }
    
    
    //public methods
    public void addInfoTo(String market, String webpageid, String url)
    {
        datastruct elem = dat.get(market);
        elem.infoAdd(webpageid, url);
    }
    
    public void removeInfoFrom(String market, String webpageid)
    {
        datastruct elem = dat.get(market);
        elem.infoRemove(webpageid);
    }
    
    public void modifyInfoIn(String market, String webpageid, String url)
    {
        datastruct elem = dat.get(market);
        elem.infoUpdate(webpageid, url);
    }
    
    public void addMarket(String market)
    {
        datastruct tmp = new datastruct();
        tmp.exchSet(market);
        dat.put(tmp.exchGet(), tmp);
    }
    
    public void removeMarket(String market)
    {
        dat.remove(market);
    }
    
    public void saveToFile(String path)
    {
        writer.writeTo(path, dat);
    }
    
    public void saveToDefaultFile()
    {
        writer.writeTo(DEFAULTPATH, dat);
    }
    
    public void readFromFile(String path)
    {
        reader.readFrom(path, dat);
    }
    
    public void readFromDefaultFile()
    {
        reader.readFrom(DEFAULTPATH, dat);
    }
    
    public final Set<String> getMarketSet()
    {
        return dat.keySet();
    }
    
    public final Set<String> getWebpageIDSetOfMarket(String market)
    {
        return dat.get(market).infoGetKeysAsSet();
    }
    
    public final String getURLOfWebpageIDFromMarket(String market, String webpageid)
    {
        return dat.get(market).infoGetSingle(webpageid);
    }
    
    public void debugPrintContents()
    {
        //test print
        Set<String> datkeyset = dat.keySet();
        Iterator<String> datitr = datkeyset.iterator();
        while(datitr.hasNext())
        {
            String datkey = datitr.next();
            datastruct elem = dat.get(datkey);
            System.out.println(elem.exchGet());
            Set<String> keyset = elem.infoGetKeysAsSet();
            Iterator<String> keysetitr = keyset.iterator();
            while(keysetitr.hasNext())
            {
                String key = keysetitr.next();
                String val = elem.infoGetSingle(key);
                System.out.println(key + ", " + val);
            }
        }
        //end test print
    }
    
    public void debugRunDriver()
    {
        debugGenerateData();
        saveToDefaultFile();
        clearData();
        readFromDefaultFile();
        
        debugPrintContents();
    }
}
