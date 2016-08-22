package main.java.com.utils;


import jdk.nashorn.internal.scripts.JO;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.*;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.java.com.utils.JunitXML;
import main.java.com.utils.XMLParserSAX;


public class Handler extends DefaultHandler{
	/** Constants used for JAXP 1.2 */
    static final String JAXP_SCHEMA_LANGUAGE =
        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static final String W3C_XML_SCHEMA =
        "http://www.w3.org/2001/XMLSchema";
    static final String JAXP_SCHEMA_SOURCE =
        "http://java.sun.com/xml/jaxp/properties/schemaSource";

    private List<JunitXML> tcList=null;
    private JunitXML tc=null;
    private String JobName=null;
    private String jobName=null;
    
    public List<JunitXML> getResults(){
    	return tcList;
    }
    
    /** A Hashtable with tag names as keys and Integers as values */
    @SuppressWarnings("rawtypes")
	private Hashtable tags;
    
 // Parser calls this once at the beginning of a document
    @SuppressWarnings("rawtypes")
	public void startDocument() throws SAXException {
        tags = new Hashtable();
    }

    boolean bName = false;
    boolean bDuration = false;    
    boolean bSkip = false;
    boolean bTestName = false;
    boolean bClassName = false;
    boolean bError = false;
    boolean oneTime=false;
    int durationCount=0;
    
    // Parser calls this for each element in a document
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        String key = localName;
        /* List of attributes
         * Would be a total of 5 (Duration,Fail,Skip,Pass,Total)
        */
        if (key.equalsIgnoreCase("file") & !oneTime) {
            oneTime = true;
        }
        if (key.equalsIgnoreCase("case")) {
            tc = new JunitXML();
            if (tcList == null) {
                tcList = new ArrayList<>();
            }
            tc.setJobname(jobName);
            tc.setIdentifier(JobName);
            tc.setBuildno(XMLParserSAX.getJobNumber());
            tc.setEnv(XMLParserSAX.getEnv());
        }else if (key.equalsIgnoreCase("duration")){
            durationCount++;
            if (durationCount>1) {
                bDuration = true;
            }
        }else if (key.equalsIgnoreCase("className")){
        	bClassName = true;
        }else if (key.equalsIgnoreCase("skipped")){
        	bSkip = true;
        }else if (key.equalsIgnoreCase("errorStackTrace")){
        	bError = true;
        }
    }
    
    // Parser calls this once after parsing a document
    @Override
    public void characters(char ch[], int start, int length) throws SAXException{
    	if (bDuration){
            if ((Double.parseDouble(tc.getDuration()))==0){
                    tc.setDuration (Double.parseDouble(new String(ch,start,length)));
            }
    		bDuration = false;
    	}else if (bClassName){
            // Look for the position of the last . to get the name of the tc
    		// clean the value to be saved
    		String str = (new String(ch,start,length));
            int lastDot =  str.lastIndexOf(".")+1;
    		int slen = (new String(ch,start,length)).length();
    		tc.setName(str.substring(lastDot, slen));
    		tc.setPass(Byte.parseByte("1"));
    		//tc.setIdentifier(JobName);
    		try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		tc.setTimestamp(System.currentTimeMillis());
    		bClassName= false;
    	}else if (bSkip){
    		if ((new String(ch,start,length)).equalsIgnoreCase("true")){
    			tc.setSkip(Byte.parseByte("1"));    			
    		}    		
    		bSkip = false;
    	}else if(oneTime & JobName==null){
    		//JobName = (((new String(ch,start,length))).indexOf("workspace"))+"";
    		//JobName = (((new String(ch,start,length))).indexOf("target"))+"";
            JobName = getProjectName(new String(ch,start,length),"(workspace\\\\)(.*?)\\\\");
            jobName = JobName;
    		//JobName = (new String(ch,start,length)).substring((((new String(ch,start,length))).indexOf("workspace")+10), (((new String(ch,start,length))).indexOf("target")-1));
    		JobName += "_"+XMLParserSAX.getJobNumber();
    	}
    }

   /* public static void main(String[] args) {

        String abspath = "<file>D:\\kohls\\prop\\kcm\\workspace\\PLU_Automation_Testing\\SeleniumTests\\target\\surefire-reports\\TEST-test.java.com.kohls.app.pro.Smoke.PLU_SmokeTestSuite.xml</file>\n";

        String regex = "(workspace\\\\)(.*?)\\\\";

        String path = getProjectName(abspath,regex);
        System.out.println(path);
    }*/

    public static  String getProjectName(String absPath,String regex){


        String refer =null;

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(absPath);

        while (matcher.find()){
            refer = matcher.group(2);
        }

        return refer;
    }
    
    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException  {
    	if (localName.equalsIgnoreCase("case")){
    		tcList.add(tc);
            String line =(tc.getIdentifier()+","+tc.getName()+","+tc.getDuration()+","+tc.getPass()+","+tc.getFail()+","+tc.getSkip()+","+tc.getTimestamp()+","+tc.getJobname()+","+tc.getBuildno()+","+tc.getEnv()+System.lineSeparator());
            FileWriter fw = new FileWriter();
            fw.writeToFile (line,tc.getJobname());
    	}
    }
    
    @SuppressWarnings("rawtypes")
	public void endDocument() throws SAXException {
        Enumeration e = tags.keys();
        while (e.hasMoreElements()) {
            String tag = (String)e.nextElement();
            int count = ((Integer)tags.get(tag)).intValue();
            System.out.println("Local Name \"" + tag + "\" occurs " + count
                               + " times");
        }
    }
    
    
    
 // Error handler to report errors and warnings
    public static class MyErrorHandler implements ErrorHandler {
        /** Error handler output goes here */
        private PrintStream out;

        MyErrorHandler(PrintStream out) {
            this.out = out;
        }

        /**
         * Returns a string describing parse exception details
         */
        private String getParseExceptionInfo(SAXParseException spe) {
            String systemId = spe.getSystemId();
            if (systemId == null) {
                systemId = "null";
            }
            String info = "URI=" + systemId +
                " Line=" + spe.getLineNumber() +
                ": " + spe.getMessage();
            return info;
        }

        // The following methods are standard SAX ErrorHandler methods.
        // See SAX documentation for more info.

        public void warning(SAXParseException spe) throws SAXException {
            out.println("Warning: " + getParseExceptionInfo(spe));
        }
        
        public void error(SAXParseException spe) throws SAXException {
            String message = "Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }

        public void fatalError(SAXParseException spe) throws SAXException {
            String message = "Fatal Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
    }

}
