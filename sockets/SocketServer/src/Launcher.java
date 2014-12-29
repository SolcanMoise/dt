import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Launcher {

	public static void main(String[] args) throws IOException {

		ServerSocket s = null;
		Socket socket = null;
		try {
			String line = "";
			s = new ServerSocket(2000);
			System.out.println("Waiting for connections");
			// When a client connects to the server s.accept() returns a
			// socket that identifies the connection
			socket = s.accept();
			System.out.println("New client connected to server");
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
					true);
			System.out.println("IO ready for transmission \n");
			
			int i=0;
			while (!line.equals("STOP")) {
				i++;
				line = in.readLine();
				System.out.println("Server log: " + line);
				// Do some business logic, DB persistence				
				out.println("[ACK#"+i+" ("+line+")]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			s.close();
			if (socket != null)
				socket.close();
		}

	}

}
