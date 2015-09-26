package ChatRoot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	private List<MyChannel> all = new ArrayList<MyChannel>();

	public static void main(String[] args) throws IOException {
		new Server().start();
		System.out.println("服务器以启动．");

	}

	public void start() throws IOException {
		ServerSocket server = new ServerSocket(8888);

		while (true) {
			Socket socket = server.accept();
			MyChannel channel = new MyChannel(socket);
			all.add(channel);
			new Thread(channel).start();

		}
	}

	class MyChannel implements Runnable {
		private DataInputStream dis;
		private DataOutputStream dos;
		private boolean isRunning = true;
		private String name;

		public MyChannel(Socket client) {
			try {
				dis = new DataInputStream(client.getInputStream());
				dos = new DataOutputStream(client.getOutputStream());
				this.name = dis.readUTF();
				this.Send("欢迎进入聊天室！");
				SendOthers(this.name + "进入了聊天室！", true);
			} catch (IOException e) {
				// e.printStackTrace();
				CloseUtil.closeAll(dis, dos);
				isRunning = false;

			}

		}

		private String Receive() {
			String msg = "";
			try {
				msg = dis.readUTF();
			} catch (IOException e) {
				// e.printStackTrace();
				CloseUtil.closeAll(dis, dos);
				isRunning = false;
				all.remove(this);
				SendOthers(this.name + " 离开了聊天室！！！", true);
			}
			return msg;

		}

		private void Send(String msg) {
			if (msg == null || msg == "") {
				return;
			}
			try {
				dos.writeUTF(msg);
				dos.flush();

			} catch (IOException e) {
				// e.printStackTrace();
				CloseUtil.closeAll(dis, dos);
				isRunning = false;
				all.remove(this);
			}
		}

		private void SendOthers(String msg, boolean sys) {
			if (msg.startsWith("@") && msg.indexOf(":") > -1) {
				String name = msg.substring(1, msg.indexOf(":"));
				String content = msg.substring(msg.indexOf(":") + 1);
				for (MyChannel other : all) {
					if (other.name.equals(name)) {
						other.Send(this.name + "对您悄悄说：" + content);

					}

				}
			} else {
				for (MyChannel tmp : all) {
					if (tmp == this) {
						continue;
					}
					if (sys) {
						tmp.Send("系统消息：" + msg);
						
					}else{
						tmp.Send(this.name + "对所有人说：" + msg);
					}

				}
			}

		}

		@Override
		public void run() {
			while (isRunning) {
				SendOthers(Receive(), false);
			}

		}

	}

}
