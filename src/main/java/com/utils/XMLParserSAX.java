package main.java.com.utils;

import java.io.File;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.XMLReader;

import main.java.com.utils.Handler.MyErrorHandler;

public class XMLParserSAX {
	
	private static String file=null;
	protected static final String leftBound="builds";
	protected static final String rightBound="/";
	static String jobnumber;
	static String env;


	/**
     * Convert from a filename to a file URL.
     */
    private static String convertToFileURL(String filename) {
        // On JDK 1.2 and later, simplify this to:
        // "path = file.toURL().toString()".
        String path = new File(filename).getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "file:" + path;
    }
    
    public static String getJobNumber(){
    	if (jobnumber.length()<=0){
    		return "000";
    	}
		return jobnumber;
    }

	public static String getEnv(){
		return env;
	}
	
    
	static public void main(String [] args) throws Exception{
    	/*String filename="C:\\Temporal\\junitResult.xml";
		jobnumber="235";
		env="QA";*/
    	String filename=null;
    	filename = args[0];
		jobnumber = args[1];
		env = args[2];
    	try{    		
    		/*
	    	for (int i=0; i<args.length; i++){
	    		filename = args[i];
	    	}*/
	    	
	    	if (filename!=null){
	    		// Create the object for the file class
	    		setFile(filename);    		
	    		//Create a JAX parser factory and configure it
	    		SAXParserFactory _pf =  SAXParserFactory.newInstance();
	    		
	    		// Set namespaceAware to true to get a parser that corresponds to
	            // the default SAX2 namespace feature setting.  This is necessary
	            // because the default value from JAXP 1.0 was defined to be false.
	            _pf.setNamespaceAware(true);
	            SAXParser _parser = _pf.newSAXParser();
	         // Set the ContentHandler of the XMLReader
	            XMLReader xmlReader = _parser.getXMLReader();
	            Handler myHandler = new Handler();
	            xmlReader.setContentHandler(myHandler);
	
	            // Set an ErrorHandler before parsing
	            xmlReader.setErrorHandler(new MyErrorHandler(System.err));
	
	            // Tell the XMLReader to parse the XML document
	            xmlReader.parse(convertToFileURL(filename));
	            List<JunitXML> tcList = myHandler.getResults();
	            for (JunitXML tc: tcList){
	            	//fw.writeToFile((String) System.out.println(tc));	            	
	            	System.out.println(tc);
	            }
	            
	    	}else{
	    		System.out.println("No file has been provided");
	    	}
    	}catch (ArrayIndexOutOfBoundsException e){
            System.out.println("ArrayIndexOutOfBoundsException caught");
        }catch (Exception ex){
			ex.printStackTrace();
		}
    }

	public static String getFile() {
		return file;
	}

	public static void setFile(String filename) {
		file = filename;
	}
}
