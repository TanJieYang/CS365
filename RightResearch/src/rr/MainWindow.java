/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rr;

import eac.UIContext;
import eac.WebService;
import eac.YahooStockDownloader;
import info.DownloadCompanyInfo;
import info.DownloadCorpAction;
import info.DownloadISINInfo;
import info.DownloadIndices;
import info.DownloadSTICons;

import info.DownloadSectorSummary;
import info.DownloadStockInfo;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;

import eac.AttributeHolder;
import eac.RegexConverter;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import stockwatch.CompanyNameUpdater;

/**
 *
 * @author tsh
 */
public class MainWindow extends javax.swing.JFrame implements Runnable, UIContext{


    private Configuration   config;
    
    private int             m_downloadIndex;
    private int             m_fileIndex;

    StringBuilder           m_debugTextSB;
    StringBuilder           m_debugTextYahoo;
    
    AtomicBoolean           m_onJob;
    AtomicBoolean           m_onJobYahoo;
    
    ArrayList<WebService>   m_services;
    ArrayList<String>       m_symbols;
    
    
    CompanyNameUpdater      m_companyWatch;
    
    @Override
    public void run()
    {
        doDownload();
        
        while(m_onJob.compareAndSet(true, false)){}

    }
    
    @Override
    public void AddDisplayText(String text)
    {
         m_debugTextSB.append("<html>" + text + "<html><br>");
        jLabel8.setText(m_debugTextSB.toString());
    }
    
    @Override
    public void DisplayText(String optional)
    {
        if(optional != null)
        {
            jLabel8.setText(m_debugTextSB.toString() + "<html>" + optional + "<html><br>");
        }
    }
    
    @Override
    public void ClearDisplayText()
    {
        m_debugTextSB.setLength(0);
        jLabel8.setText("");
    }
    
    @Override
    public void SetProgressVal(int v)
    {
        jProgressBar1.setValue(v);
    }
    

    
    /**
     * Creates new form RRDataAggregatorWindow
     */
    public MainWindow() 
    {
        config = new Configuration ();
        
        

        initComponents();
        
        myInitComponents();
        m_downloadIndex = 0;
        m_fileIndex = 0;
        
        jComboBox1.setEnabled(false);
        jComboBox3.setEnabled(false);
        jRadioButton2.setEnabled(false);
        jRadioButton1.setEnabled(false);
        
        m_debugTextSB = new StringBuilder();
        m_debugTextYahoo = new StringBuilder();
        
        m_onJob = new AtomicBoolean();
        m_onJob.set(false);
        
        m_onJobYahoo = new AtomicBoolean();
        m_onJobYahoo.set(false);
        
        m_services = new ArrayList<>();
        
        m_services.add(new DownloadSectorSummary());
        m_services.add(new DownloadIndices());
        m_services.add(new DownloadSTICons());
        m_services.add(new DownloadCorpAction());
        m_services.add(new DownloadStockInfo());
        m_services.add(new DownloadISINInfo());
        m_services.add(new DownloadCompanyInfo());
        
        
        jRadioButton8.setSelected(true);

        //populate list for finance yahoo download
        AttributeHolder companySymbols = new AttributeHolder();
        companySymbols.AddAttribute("Symbol");
        companySymbols.AddAttribute("Name");
        
        companySymbols.InitJSONArray(config.GetRawCompanySymbolInfo(), "Comp");
        
        m_symbols = companySymbols.GetElements("Symbol");
        ArrayList<String> names = companySymbols.GetElements("Name");
        
        DefaultListModel listModel = new DefaultListModel();
        
        
        jList3.removeAll();
        
        for(int i = 0; i < companySymbols.GetMaxElem(); ++i)
        {
            listModel.addElement(names.get(i).replaceAll("\"", ""));
        }
        
        jList3.setModel(listModel);
        
        jTabbedPane1.setTitleAt(0, "Webpage Download");
        jTabbedPane1.setTitleAt(1, "Stock Watch List");
        jTabbedPane1.setTitleAt(2, "Historical Price Data");
        jTabbedPane1.setTitleAt(3, "Background Jobs");
    }

    private void myInitComponents()
    {
        
        String [] data = null;
        
        data = config.getTAB1_SOURCE_EXCHANGE_COMBOBOX_CONTENTS();
        if (data != null)
        {
            jComboBox1.removeAllItems();
            for (String anItem : data)
                jComboBox1.addItem (anItem);
        }
        
        data = config.getTAB1_SOURCE_SGX_TYPESOFINFO_COMBOBOX_CONTENTS();
        if (data != null)
        {
            jComboBox2.removeAllItems();
            for (String anItem : data)
                jComboBox2.addItem (anItem);
        }

        data = config.getTAB1_SOURCE_SGX_URL();
        if (data != null)
        {
            jComboBox3.removeAllItems();
            for (String anItem : data)
                jComboBox3.addItem (anItem);
        }

        data = config.getTAB1_DESTINATION_FILEFORMAT_COMBOBOX_CONTENTS();
        if (data != null)
        {
            jComboBox4.removeAllItems();
            for (String anItem : data)
                jComboBox4.addItem (anItem);
        }
        
        
        ALL_Listeners al = new ALL_Listeners ();
        jRadioButton1.addActionListener(al);
        jRadioButton2.addActionListener(al);
        jRadioButton3.addActionListener(al);
        jRadioButton4.addActionListener(al);
        jRadioButton5.addActionListener(al);
        jRadioButton6.addActionListener(al);
        jRadioButton7.addActionListener(al);
        jButton1.addActionListener(al);
        
        jComboBox1.addItemListener(al);
        jComboBox2.addItemListener(al);
        jComboBox3.addItemListener(al);
        jComboBox4.addItemListener(al);
        jComboBox5.addItemListener(al);
        jComboBox6.addItemListener(al);
        
        jTimeButton1.addPropertyChangeListener(al);
        jTimeButton2.addPropertyChangeListener(al);
        
        jRadioButton1.setSelected(true);
        jRadioButton2.setSelected(false);
        
        jRadioButton3.setSelected(true);
        jRadioButton4.setSelected(false);

        jRadioButton5.setSelected(true);
        jRadioButton6.setSelected(false);
        jRadioButton7.setSelected(false);
        
        jTextField1.setEnabled (false);
        jTextField2.setEnabled (false);
        jTextField3.setEnabled (false);
        
        
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] 
                {   "<WebPageID>-<Year>-<Month>-<Day>", 
                    "<WebPageID>-<Year><Month><Day>-<Hr><Min>hrs", 
                    "<WebPageID>-<Day>_<Month>_<Year>", 
                    "<Sn>-<WebPageID>-<Year>-<Month>-<Day>"
        
        
                }));
        
        jList2.removeAll();
        
        m_companyWatch = new CompanyNameUpdater(jButton3, jList2);
        m_companyWatch.Begin();
        
    }
    
    
    
    //threaded function
    private void doDownload()
    {

        ClearDisplayText();
        SetProgressVal(0);
        AddDisplayText("initiating connection ...");
        
        StringBuilder fileName = new StringBuilder();
        StringBuilder fileDir = new StringBuilder();
        
        //determine file directory
        if(jRadioButton5.isSelected()) //combo box default paths
        {
            fileDir.append(System.getProperty("user.dir") + File.separator);
        }
                
        else if(jRadioButton6.isSelected()) //user defined absolute path
        {
            File file = new File(jTextField3.getText());
            
            if(!file.isDirectory())
            {
                AddDisplayText("Directory does not exists, saved to default directory!");
                fileDir.append(System.getProperty("user.dir") + File.separator);
                
            }
            else
                fileDir.append(jTextField3.getText() + File.separator);
        }
        else if(jRadioButton7.isSelected())
        {
            System.out.println("HEY");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new java.io.File("."));
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            
            int result = fileChooser.showSaveDialog(this);
            
            if(result == JFileChooser.APPROVE_OPTION)
            {
               
               fileDir.append(fileChooser.getSelectedFile() + File.separator);
            }
            
            else if(result == JFileChooser.CANCEL_OPTION)
            {
                AddDisplayText("No File Path Selected!");
                return;
            }
        }
            
        
        
        int selectIndex = jComboBox4.getSelectedIndex();
        
        
        String fileEx = "";
        
        switch(selectIndex)
        {
            case 0:
                fileEx = new String(".csv");
                break;
            case 1:
                fileEx = new String(".tld");
                break;
            case 2:
                fileEx = new String(".xls");
                break;
            default:
                
                break;
        }
        
        RegexConverter regX = new RegexConverter();
        if(jRadioButton4.isSelected())
        {
                String regex = jTextField2.getText();
            try {
                fileName.append(regX.convertRegex(regex, (String)jComboBox2.getSelectedItem(),
                                  "SGX", Paths.get(fileDir.toString()), fileEx));
            } catch (IOException ex) 
            {
                AddDisplayText(ex.toString());
                return;
            }
            
                
        }
            
        else if(jRadioButton3.isSelected())
        {
                String regex = (String)jComboBox5.getSelectedItem();
                try {
                fileName.append(regX.convertRegex(regex, (String)jComboBox2.getSelectedItem(),
                                  "SGX", Paths.get(fileDir.toString()), fileEx));
                } catch (IOException ex) 
                {
                    AddDisplayText(ex.toString());
                    return;
                }
        }
        
        
        jLabel8.setText("Downloading info: " + jComboBox2.getSelectedItem() + " from SGX");
        
        
        /*********************************************************************/
        //not exactly sure how you used SGXSourceDownload without creating it...
        //need to pass in user defined company name
        YahooStockDownloader YSD = new YahooStockDownloader("IBM");
        //can change company name using: YSD.SetCompanyName("TSLA");
        
        String finalFile = fileDir.toString() + fileName.toString();
        
        if(m_downloadIndex >= 0 && m_downloadIndex < m_services.size())
        {
            WebService service = m_services.get(m_downloadIndex);
            
            service.On_Initialize(this);
            service.On_DownloadData(this);
            AddDisplayText("parsing / intepreting data ...");
            service.On_FormatData(this);
            AddDisplayText("saving data ...");
            
            
            
            service.On_SaveData(this, finalFile.replace("| ", "").replace("<", "").replace(">", "").replace("MarketInfo ", "").replace("CompanyInfo ", "").replace("/'", ""), (char)selectIndex);
            AddDisplayText("download in progress ... - 100%");
            SetProgressVal(100);
            
            AddDisplayText("saved file: " + finalFile);
        }
        else
        {
            System.out.println("Service not found!");
                    
        }
        
            /*
                //this is to initiate the request with YahooFinanceAPI
                AddDisplayText("Download in progress ...");
                init = YSD.GetYahooFinanceRequest();
                //failed connection!
                if(!init) {
                    AddDisplayText("connection failed!");
                    return;
                }
                // this is to create parse the data into 3 separate list.
                //daily, weekly and monthly lists
                AddDisplayText("parsing/interpreting data ...");
                YSD.GetHistoricalQuotes(new GregorianCalendar(2010, 1, 18), new GregorianCalendar(2016, 1, 18));
                
                AddDisplayText("Saving data ..");
                //The filenames are fixed for this part
                //this is to print all the 3 list into their respective files
                YSD.PrintAllQuotes();
                AddDisplayText("Complete!!!");
                break;
*/
        
        
    }

// #################################################################    
// Start - Inner Class
// #################################################################    

    private class ALL_Listeners implements ActionListener, java.awt.event.ItemListener, PropertyChangeListener
    {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            SimpleDateFormat parseFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
            SimpleDateFormat printFormat = new SimpleDateFormat("hh:mm a");
            Date date = null;
            
            if(evt.getNewValue() != null && evt.getSource().equals(jTimeButton1)) {
                    try {
                        date = parseFormat.parse(evt.getNewValue().toString());
                        jTextField5.setText(printFormat.format(date).toString());
                    } 
                    catch (ParseException ex) {
                    }
            }
            if(evt.getNewValue() != null && evt.getSource().equals(jTimeButton2)) {
                try {
                    date = parseFormat.parse(evt.getNewValue().toString());
                    jTextField6.setText(printFormat.format(date).toString());
                } 
                catch (ParseException ex) {
                }
            }
        }
        
        public ALL_Listeners ()
        {

        }
    
        @Override
        public void actionPerformed (ActionEvent ae)
        {

            String className = ae.getSource().getClass().getName();
        
            
            if(ae.getSource().equals(jButton1))
            {
                
                //check if open dialog
                
                if(m_onJob.compareAndSet(false, true))
                {
                    Thread jobThread = new Thread(MainWindow.this);
                    jobThread.start();
                }

            }
            
            if (className.equalsIgnoreCase("javax.swing.JRadioButton"))
                processRadioButtons (ae.getSource());
        
        }
    
        @Override
        public void itemStateChanged (ItemEvent ie)
        {
            String className = ie.getSource().getClass().getName();
            
            if(ie.getSource().equals(jComboBox2))
            {
                int selectIndex = jComboBox2.getSelectedIndex();
                if(selectIndex == 6)
                {
                    jComboBox4.setEnabled(false);
                    jComboBox4.addItem(".html");
                    jComboBox4.setSelectedItem(".html");
                }
                else
                {
                    jComboBox4.setEnabled(true);
                    jComboBox4.removeItem(".html");
                    jComboBox4.setSelectedIndex(0);
                }
            }
            
            if (className.equalsIgnoreCase("javax.swing.JComboBox"))
                processComboBoxes (ie.getSource());
        
        
        }
    
        private void processRadioButtons (Object obj)
        {
            JRadioButton rb = (JRadioButton) obj;
            
            String text = rb.getText();        
            System.out.println ("rb text : " + text);
        
            if (rb == jRadioButton1)
            {
                jComboBox3.setEnabled(true);
                jTextField1.setEnabled (false);
            }
            else if (rb == jRadioButton2)
            {
                jComboBox3.setEnabled(false);
                jTextField1.setEnabled (true);
            }           
            else if (rb == jRadioButton3)
            {
                jComboBox5.setEnabled(true);
                jTextField2.setEnabled (false);
            }
            else if (rb == jRadioButton4)
            {
                jComboBox5.setEnabled(false);
                jTextField2.setEnabled (true);
            }
            else if (rb == jRadioButton5)
            {
                jComboBox6.setEnabled(true);
                jTextField3.setEnabled (false);
            }
            else if (rb == jRadioButton6)
            {
                jComboBox6.setEnabled(false);
                jTextField3.setEnabled (true);
            }
            else if (rb == jRadioButton7)
            {
                jComboBox6.setEnabled(false);
                jTextField3.setEnabled (false);
            }
            
        }
    
        private void processComboBoxes (Object obj)
        {
            JComboBox cb = (JComboBox) obj;
            String selected = (String) cb.getSelectedItem();
            m_fileIndex = cb.getSelectedIndex();
            //System.out.println ("cb selected : '" + selected + "'" + " selected Index: " + m_fileIndex);
            

            
            if (cb == jComboBox1)
            {
                System.out.println ("jComboBox1 is selected!!");
                
                String [] data  = null;
                String [] data1 = null;
                
                if (selected.equalsIgnoreCase("SGX"))
                {
                    data  = config.getTAB1_SOURCE_SGX_TYPESOFINFO_COMBOBOX_CONTENTS();
                    data1 = config.getTAB1_SOURCE_SGX_URL();
                }
                else if (selected.equalsIgnoreCase("KLSE"))
                {
                    data  = config.getTAB1_SOURCE_KLSE_TYPESOFINFO_COMBOBOX_CONTENTS();
                    data1 = config.getTAB1_SOURCE_KLSE_URL();
                }
                else if (selected.equalsIgnoreCase("NYSE"))
                {
                    data  = config.getTAB1_SOURCE_NYSE_TYPESOFINFO_COMBOBOX_CONTENTS();
                    data1 = config.getTAB1_SOURCE_NYSE_URL();
                }
                else;
                
                if (data != null)
                {
                    
                    jComboBox2.removeAllItems();
                    for (String anItem : data)
                        jComboBox2.addItem (anItem);
                }
                                    
                if (data1 != null)
                {
                    jComboBox3.removeAllItems();
                    for (String anItem : data1)
                        jComboBox3.addItem (anItem);
                }
            }
            
            else if (cb == jComboBox2)
            {
                //display url
                jComboBox3.setSelectedIndex(jComboBox2.getSelectedIndex());
                
                m_downloadIndex = jComboBox2.getSelectedIndex();
                
            }
            else if (cb == jComboBox3)
            {

                System.out.println ("jComboBox3 is selected!!");
            }
                
            else if (cb == jComboBox4)
            {
                System.out.println ("jComboBox3 is selected!!");
            }
        
        
        }    
    
    }       // end class ALL_Listeners ...
    
    
    
// #################################################################    
// End - Inner Class
// #################################################################    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<String>();
        jLabel3 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<String>();
        jLabel1 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jComboBox3 = new javax.swing.JComboBox<String>();
        jRadioButton2 = new javax.swing.JRadioButton();
        jTextField1 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<String>();
        jLabel6 = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jComboBox5 = new javax.swing.JComboBox<String>();
        jRadioButton4 = new javax.swing.JRadioButton();
        jTextField2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jRadioButton5 = new javax.swing.JRadioButton();
        jComboBox6 = new javax.swing.JComboBox<String>();
        jRadioButton6 = new javax.swing.JRadioButton();
        jTextField3 = new javax.swing.JTextField();
        jRadioButton7 = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jTimeButton1 = new org.sourceforge.jcalendarbutton.JTimeButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTimeButton2 = new org.sourceforge.jcalendarbutton.JTimeButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel18 = new javax.swing.JLabel();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jLabel20 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        jRadioButton10 = new javax.swing.JRadioButton();
        jRadioButton11 = new javax.swing.JRadioButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jLabel9 = new javax.swing.JLabel();
        jProgressBar2 = new javax.swing.JProgressBar();
        jButton2 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(760, 560));

        jPanel1.setBackground(new java.awt.Color(100, 100, 100));
        jPanel1.setName("jTabbedPane1_MainPanel"); // NOI18N

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel2.setText("Stock Exchange : ");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 49, -1, -1));

        jComboBox1.setFont(new java.awt.Font("Courier New", 0, 12));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel2.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(147, 49, 580, -1));

        jLabel3.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel3.setText("Type Of Info : ");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 119, -1));

        jComboBox2.setFont(new java.awt.Font("Courier New", 0, 12));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel2.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 580, -1));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Specify Source");
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 720, -1));

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Courier New", 0, 12));
        jRadioButton1.setText("Use Pre-Configured URL below : ");
        jPanel2.add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 672, -1));

        jComboBox3.setFont(new java.awt.Font("Courier New", 0, 12));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel2.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 720, -1));

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Courier New", 0, 12));
        jRadioButton2.setText("Use the URL I typed in below : ");
        jPanel2.add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 672, -1));

        jTextField1.setFont(new java.awt.Font("Courier New", 0, 12));
        jTextField1.setText("jTextField1");
        jTextField1.setVerifyInputWhenFocusTarget(false);
        jPanel2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 720, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Specify Destination");
        jLabel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel5.setText("Saved File Format : ");

        jComboBox4.setFont(new java.awt.Font("Courier New", 0, 12));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel6.setText("Saved File Name : ");

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setFont(new java.awt.Font("Courier New", 0, 12));
        jRadioButton3.setText("Use Pre-Configured Naming Format : ");

        jComboBox5.setFont(new java.awt.Font("Courier New", 0, 12));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setFont(new java.awt.Font("Courier New", 0, 12));
        jRadioButton4.setText("Use the file name (on the right) =>");

        jTextField2.setFont(new java.awt.Font("Courier New", 0, 12));
        jTextField2.setText("jTextField2");

        jLabel7.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel7.setText("Saved File Location : ");

        buttonGroup3.add(jRadioButton5);
        jRadioButton5.setFont(new java.awt.Font("Courier New", 0, 12));
        jRadioButton5.setText("Use Pre-Configured Folder Path : ");

        jComboBox6.setFont(new java.awt.Font("Courier New", 0, 12));
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        buttonGroup3.add(jRadioButton6);
        jRadioButton6.setFont(new java.awt.Font("Courier New", 0, 12));
        jRadioButton6.setText("Use the Absolute Folder Path (typed in) below : ");

        jTextField3.setFont(new java.awt.Font("Courier New", 0, 12));
        jTextField3.setText("jTextField3");

        buttonGroup3.add(jRadioButton7);
        jRadioButton7.setFont(new java.awt.Font("Courier New", 0, 12));
        jRadioButton7.setText("Open a File - Save Dialog");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jRadioButton5)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jRadioButton6)
                        .addGap(107, 107, 107)
                        .addComponent(jRadioButton7))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRadioButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRadioButton3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                            .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(17, 17, 17)
                        .addComponent(jLabel6))
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton3)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton5)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton6)
                    .addComponent(jRadioButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jButton1.setFont(new java.awt.Font("Arial Black", 2, 12));
        jButton1.setText("Retrieve Data");

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel8.setText("Download Progress Messages ...");
        jLabel8.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 755, Short.MAX_VALUE)))
                .addContainerGap(308, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(178, 178, 178))
        );

        jScrollPane1.setViewportView(jPanel1);

        jTabbedPane1.addTab("tab1", jScrollPane1);

        jPanel4.setBackground(new java.awt.Color(100, 100, 100));

        jPanel5.setPreferredSize(new java.awt.Dimension(791, 200));

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(jList2);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Stock Watchlist");
        jLabel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel10.setText("Start Date :");

        jLabel12.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel12.setText("Company    :");

        jLabel13.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel13.setText("End Date   :");

        jButton3.setFont(new java.awt.Font("Verdana", 1, 14));
        jButton3.setText("Add");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Verdana", 1, 14));
        jButton4.setText("Delete");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Verdana", 1, 14));
        jButton5.setText("Update");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jTimeButton1.setText("Start");
        jTimeButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTimeButton1ActionPerformed(evt);
            }
        });

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList1);

        jLabel14.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel14.setText("Start Time :");

        jTextField5.setFont(new java.awt.Font("Courier New", 0, 12));
        jTextField5.setText("StartTimeField");
        jTextField5.setVerifyInputWhenFocusTarget(false);
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel15.setText("End Time   :");

        jTextField6.setFont(new java.awt.Font("Courier New", 0, 12));
        jTextField6.setText("EndTimeField");
        jTextField6.setVerifyInputWhenFocusTarget(false);
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jTimeButton2.setText("End");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel12)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane5))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel14)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTimeButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel10)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGap(95, 95, 95)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel15)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTimeButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(26, 26, 26)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTimeButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel14)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTimeButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel15)
                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel12))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 749, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(136, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(436, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel4);

        jTabbedPane1.addTab("tab2", jScrollPane2);

        jPanel6.setBackground(new java.awt.Color(100, 100, 100));

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 18));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Yahoo Finance SGX");
        jLabel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel18.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel18.setText("Start Date   :");

        jLabel20.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel20.setText("End Date :");

        jLabel19.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel19.setText("Comapny Name :");

        jRadioButton8.setText("Daily");

        jRadioButton9.setText("Weekly");

        jRadioButton10.setText("Monthly");

        jRadioButton11.setText("Dividends");

        jList3.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane6.setViewportView(jList3);

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel9.setText("Download Progress Messages ...");
        jLabel9.setOpaque(true);

        jButton2.setFont(new java.awt.Font("Arial Black", 2, 12));
        jButton2.setText("Retrieve Data");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jRadioButton8)
                        .addGap(36, 36, 36)
                        .addComponent(jRadioButton9)
                        .addGap(26, 26, 26)
                        .addComponent(jRadioButton10)
                        .addGap(27, 27, 27)
                        .addComponent(jRadioButton11)
                        .addContainerGap())
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel18)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jLabel19)
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                            .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                                            .addComponent(jLabel20)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)))
                                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE))
                            .addGap(51, 51, 51))
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(jButton2)
                            .addGap(18, 18, 18)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jProgressBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(51, Short.MAX_VALUE)))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel19))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton8)
                    .addComponent(jRadioButton9)
                    .addComponent(jRadioButton10)
                    .addComponent(jRadioButton11))
                .addGap(31, 31, 31)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(168, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(455, Short.MAX_VALUE))
        );

        jScrollPane4.setViewportView(jPanel6);

        jTabbedPane1.addTab("tab3", jScrollPane4);

        jScrollPane7.setBackground(new java.awt.Color(102, 102, 102));

        jPanel8.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 877, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 837, Short.MAX_VALUE)
        );

        jScrollPane7.setViewportView(jPanel8);

        jTabbedPane1.addTab("tab4", jScrollPane7);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 867, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTimeButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTimeButton1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jTimeButton1ActionPerformed

    
    
    
    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        Date startDate = jDateChooser3.getDate();
        Date endDate = jDateChooser4.getDate();
        
        jLabel9.setText("");
        m_debugTextYahoo.setLength(0);

        boolean isValid = true;
        int selectedIndex = -1;
        if(startDate == null || endDate == null)
        {
            m_debugTextYahoo.append(" Invalid Date! ");
            isValid = false;
        }
        
        selectedIndex = jList3.getSelectedIndex();
        
        if(selectedIndex == -1 || selectedIndex >= this.m_symbols.size())
        {
            m_debugTextYahoo.append(" Invalid Company Selection! ");
            isValid = false;
        }
        
        //check if daily/weekly/monthly/dividends buttons are selected
        if(!jRadioButton8.isSelected() && !jRadioButton9.isSelected() && 
           !jRadioButton10.isSelected() && !jRadioButton11.isSelected())
        {
            m_debugTextYahoo.append(" Invalid Filetype Selection! ");
            isValid = false;
        }
        
        
        if(!isValid)
        {
            jLabel9.setText(m_debugTextYahoo.toString());
            return;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        
        final int start_year    = cal.get(Calendar.YEAR);
        final int start_month   = cal.get(Calendar.MONTH);
        final int start_day     = cal.get(Calendar.DAY_OF_MONTH);
        
        cal.setTime(endDate);
        
        final int end_year    = cal.get(Calendar.YEAR);
        final int end_month   = cal.get(Calendar.MONTH);
        final int end_day     = cal.get(Calendar.DAY_OF_MONTH);
        
        final String companySymbol = m_symbols.get(selectedIndex).replace("\"", "");
        
        /*
                //this is to initiate the request with YahooFinanceAPI
                AddDisplayText("Download in progress ...");
                init = YSD.GetYahooFinanceRequest();
                //failed connection!
                if(!init) {
                    AddDisplayText("connection failed!");
                    return;
                }
                // this is to create parse the data into 3 separate list.
                //daily, weekly and monthly lists
                AddDisplayText("parsing/interpreting data ...");
                YSD.GetHistoricalQuotes(new GregorianCalendar(2010, 1, 18), new GregorianCalendar(2016, 1, 18));
                
                AddDisplayText("Saving data ..");
                //The filenames are fixed for this part
                //this is to print all the 3 list into their respective files
                YSD.PrintAllQuotes();
                AddDisplayText("Complete!!!");
                break;
*/
        if(this.m_onJobYahoo.compareAndSet(false, true))
        {
            Thread jobThread = new Thread(new Runnable()
                    {
                        public void run()
                        {
                            jProgressBar2.setValue(0);
                            m_debugTextYahoo.setLength(0);
                            m_debugTextYahoo.append("<html>initiating connection ...<html><br>");
                            jLabel9.setText(m_debugTextYahoo.toString());
                            
                            YahooStockDownloader ysd = new YahooStockDownloader(companySymbol);
                            GregorianCalendar startGreg = new GregorianCalendar(start_year, start_month+1, start_day);
                            GregorianCalendar endGreg = new GregorianCalendar(end_year, end_month + 1, end_day);

                            if(!ysd.GetYahooFinanceRequest() || !ysd.GetDividendRequest(startGreg, endGreg))
                            {
                                m_debugTextYahoo.append("<html>connection failed!<html><br>");
                                jLabel9.setText(m_debugTextYahoo.toString());
                            }
                            else
                            {
                                jProgressBar2.setValue(25);
                                if(jRadioButton8.isSelected())  //daily
                                {
                                    
                                    m_debugTextYahoo.append("<html>parsing/interpreting data ...<html><br>");
                                    jLabel9.setText(m_debugTextYahoo.toString());
                                    ysd.GetDailyHistorical(startGreg, endGreg);
                                    
                                    if(ysd.PrintDailyQuotes()) 
                                    {
                                        m_debugTextYahoo.append("<html>Saving data ..<html><br>");
                                        jLabel9.setText(m_debugTextYahoo.toString());
                                        
                                        m_debugTextYahoo.append("<html>Daily Quotes Printed!!!<html><br>");
                                    }
                                    else {
                                        m_debugTextYahoo.append("<html>Unable to retrieve Daily Quotes...<html><br>");
                                    }
                                    
                                }
                                jProgressBar2.setValue(35);
                                if(jRadioButton9.isSelected()) { //weekly
                                    
                                    m_debugTextYahoo.append("<html>parsing/interpreting data ...<html><br>");
                                    jLabel9.setText(m_debugTextYahoo.toString());
                                    ysd.GetWeeklyHistorical(startGreg, endGreg);
                                    
                                    if(ysd.PrintWeeklyQuotes()) {
                                        m_debugTextYahoo.append("<html>Saving data ..<html><br>");
                                        jLabel9.setText(m_debugTextYahoo.toString());
                                        
                                        m_debugTextYahoo.append("<html>Weekly Quotes Printed!!!<html><br>");
                                    }
                                    else {
                                        m_debugTextYahoo.append("<html>Unable to retrieve Weekly Quotes...<html><br>");
                                    }
                                }
                                jProgressBar2.setValue(50);
                                if(jRadioButton10.isSelected()) { //monthly
                                    
                                    m_debugTextYahoo.append("<html>parsing/interpreting data ...<html><br>");
                                    jLabel9.setText(m_debugTextYahoo.toString());
                                    ysd.GetMonthlyHistorical(startGreg, endGreg);
                                    
                                    if(ysd.PrintMonthlyQuotes()) {
                                        m_debugTextYahoo.append("<html>Saving data ..<html><br>");
                                        jLabel9.setText(m_debugTextYahoo.toString());
                                        
                                        m_debugTextYahoo.append("<html>Monthly Quotes Printed!!!<html><br>");
                                    }
                                    else {
                                        m_debugTextYahoo.append("<html>Unable to retrieve Monthly Quotes...<html><br>");
                                    }
                                }
                                jProgressBar2.setValue(60);
                                if(jRadioButton11.isSelected()) { //dividend
                                    m_debugTextYahoo.append("<html>parsing/interpreting data ...<html><br>");
                                    jLabel9.setText(m_debugTextYahoo.toString());
                                    ysd.GetDividendHistorical();
                                    if(ysd.PrintDividendsQuotes()) {
                                        m_debugTextYahoo.append("<html>Saving data ..<html><br>");
                                        jLabel9.setText(m_debugTextYahoo.toString());
                                        
                                        m_debugTextYahoo.append("<html>Dividends Printed!!!<html><br>");
                                    }
                                    else {
                                        m_debugTextYahoo.append("<html>Unable to retrieve Dividends...<html><br>");
                                    }
                                }
                                jProgressBar2.setValue(75);
                                m_debugTextYahoo.append("<html>Complete!!!<html><br>");
                                jLabel9.setText(m_debugTextYahoo.toString());
                                jProgressBar2.setValue(100);      
                            }
                            
                            while(m_onJobYahoo.compareAndSet(true, false)){}
                        }
                    }
                  );
            
            jobThread.start();
        }
            
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton11;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private org.sourceforge.jcalendarbutton.JTimeButton jTimeButton1;
    private org.sourceforge.jcalendarbutton.JTimeButton jTimeButton2;
    // End of variables declaration//GEN-END:variables
}
