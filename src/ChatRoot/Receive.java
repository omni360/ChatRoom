package ChatRoot;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Receive implements Runnable{
	@Override
	public void run() {
		while(isRunning = true){
			System.out.println(Receive());
		}
		
		
	}


	private DataInputStream dis;
	private boolean isRunning = true;
	public Receive(){
		
	}
	
	public Receive(Socket client){
		try {
			dis = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
//			e.printStackTrace();
			isRunning = false;
			CloseUtil.closeAll(dis);
		}
	}
	
	
	public String Receive(){
		String msg = "";
		try {
			msg = dis.readUTF();
		} catch (IOException e) {
			isRunning = false;
			CloseUtil.closeAll(dis);
		}
		
		return msg;
		
	}

}
