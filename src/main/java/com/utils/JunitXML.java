package main.java.com.utils;

//import java.sql.Date;
import java.text.DecimalFormat;
//import java.time.LocalDateTime;

public class JunitXML {
	private String identifier;
	private String name;
	private double duration=0;
	private byte fail;
	private byte skip;
	private byte pass;
	private long timestamp=0;
	private String jobname;
	private String buildno;

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	private String env;
	

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDuration() {
		DecimalFormat df = new DecimalFormat("###,###,###.####");
		return df.format(duration);
	}
	public void setDuration(double d) {		
		this.duration = d;
	}
	public byte getSkip() {
		return skip;
	}
	public void setSkip(byte skip) {
		this.skip = skip;
	}
	public byte getPass() {
		return pass;
	}
	public void setPass(byte pass) {
		this.pass = pass;
	}
	
	public byte getFail() {
		return fail;
	}
	public void setFail(byte fail) {
		this.fail = fail;
	}

	@Override
	public String toString(){
		// Header: Testcase,Duration,fail,skip,pass
		String line="";
		try{
			line =(this.getIdentifier()+","+this.getName()+","+this.getDuration()+","+this.getPass()+","+this.getFail()+","+this.getSkip()+","+this.getTimestamp()+","+this.getJobname()+","+this.getBuildno()+System.lineSeparator());
			//FileWriter fw = new FileWriter();
			//fw.writeToFile (line,this.getJobname());
		}catch (Exception ex){
			//ex.printStackTrace();
		}
		return line;
		//return this.getIdentifier()+","+this.getName()+","+this.getDuration()+","+this.getPass()+","+this.getFail()+","+this.getSkip()+","+this.getTimestamp()+","+this.getJobname()+","+this.getBuildno();
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public long getTimestamp() {		
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	public String getBuildno() {
		return buildno;
	}
	public void setBuildno(String Buildno) {
		this.buildno="";
		this.buildno = Buildno;
	}
}
