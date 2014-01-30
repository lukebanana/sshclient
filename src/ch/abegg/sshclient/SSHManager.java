package ch.abegg.sshclient;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JOptionPane;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHManager{
	
	private final String host="91.250.117.77";
    private final String user="root";
    private final String password="9G94itb4";
    private String command = null;
    private boolean keepConnection = true;
    
    private Channel channel = null;
    
    private InputStream in = null;
    private OutputStream out = null;
    private PrintStream ps = null;
	
    public SSHManager(){
    	init();
    }
    
	public void init() {
        
        try{

            java.util.Properties config = new java.util.Properties(); 
            config.put("StrictHostKeyChecking", "no");
            
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);
            session.setPassword(password);
            session.setConfig(config);
            session.connect(40000);
            System.out.println("Connected to host " + host);
             
            channel = session.openChannel("shell");
            
            channel.setInputStream(System.in);       
            
            out = channel.getOutputStream();

            ps = new PrintStream(out, true);
            
            
            channel.connect();
            
            in = channel.getInputStream();
    	
            while(keepConnection){
            	//command = askForCommand();
            	//ps.println(command); 
            	getStream();
            }
            
            channel.disconnect();
            session.disconnect();
            System.out.println("DONE");
        }catch(Exception e){
            e.printStackTrace();
        }
	}
  
	private String askForCommand() {
		String tmpcommand = JOptionPane.showInputDialog("Enter command. Type 'exit' to leave.", "ls");
    	while(tmpcommand.isEmpty()) {
    		tmpcommand = JOptionPane.showInputDialog("Enter command", "ls");
    	}
    	
    	if(tmpcommand.equals("exit")) keepConnection = false;
    	
    	return tmpcommand;
	}

	
	public void getStream() throws IOException{
		
		byte[] tmp = new byte[1024];
			  
		  // read and print out input stream
		while(true){
			while(in.available() > 0){
				int i = in.read(tmp, 0, 1024);
				if(i < 0) break;
				System.out.print(new String(tmp, 0, i));
		    }
			 
			if(channel.isClosed()){
		    	System.out.println("exit-status: " + channel.getExitStatus());
		    	keepConnection = false;
		    	break;
		    }
			
		    try{Thread.sleep(900);}catch(Exception ee){}
		}
	}
	
	
	public static void main(String[] args) {
		SSHManager ssh = new SSHManager();
	}
}