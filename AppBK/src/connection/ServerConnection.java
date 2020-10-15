package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import user.Usuario;

@SuppressWarnings("unused")
public class ServerConnection {

//	private Usuario usuario;
	private long contador;
	private Conexao threadConnection;

	public ServerConnection(String host, int port) {
		if (threadConnection == null) {
			threadConnection = new Conexao(host, port);
		}
	}

	public void setInterfaceConnectionListener(InterfaceCommand interfaceCommand) {
		threadConnection.setInterfaceConnectionListener(interfaceCommand);
	}

	public boolean connect() {
		if (threadConnection.connect()) {
			if (!threadConnection.isAlive()) {
				threadConnection.start();
			}
			contador = System.currentTimeMillis();
			return true;
		}
		return false;
	}

	public void sendCommand(String comando) {
		threadConnection.enviaComando(comando);
	}

	public void destroy(boolean reconnect) {
		threadConnection.deathThread(reconnect);
	}

	private class Conexao extends Thread {
		private long timeout = 0;
		private String host;
		private int port;
		private Socket socket;
		private String comando;
		private BufferedReader br;
		private StringBuilder stringBuilder;
		private PrintStream ps;
		private InterfaceCommand interfaceCommand;

		public Conexao(String host, int port) {
			this.host = host;
			this.port = port;
		}

		public boolean connect() {

			try {

				if (socket != null && socket.isConnected()) {
					ps.close();
					br.close();
					socket.close();
				}

				socket = new Socket();
				socket.connect(new InetSocketAddress(host, port), 3000);
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				ps = new PrintStream(socket.getOutputStream());
				stringBuilder = new StringBuilder();

			} catch (IOException e) {
				// e.printStackTrace(); Não printar uma grande quantidade de erros na tela
				// quando não conseguir se conectar
				return false;
			}
			startCommandFromInterface(InterfaceCommand.ON_CONNECTED);
			return true;
		}

		private void enviaComando(String comando) {
			this.comando = comando;
		}

		@Override
		public void run() {

			while (socket.isConnected() && !socket.isClosed()) {

				if (passouTempoDemais()) {
					deathThread(true);
				}

				try {
					if (br.ready()) {
						while (br.ready()) {
							int retorno = br.read();
							stringBuilder.append(Character.toChars(retorno));
						}
						startCommandFromInterface(InterfaceCommand.ON_COMMAND_RECEIVED);
						if (String.valueOf('\0').equals(stringBuilder.toString())) {
							contador = System.currentTimeMillis();
						}
					}

					stringBuilder.delete(0, stringBuilder.length());

					if (comando != null && !comando.isEmpty()) {
						ps.println(comando);
						ps.flush();
						comando = null;
					}

				} catch (IOException e) {
					deathThread(true);
					e.printStackTrace();
				}
			}
		}

		public void deathThread(boolean reconnect) {
			if (socket != null && socket.isConnected()) {
				try {
					timeout = 0;
					socket.close();
					ps.close();
					br.close();
					if (reconnect) {
						startCommandFromInterface(InterfaceCommand.ON_DISCONNECTED);
					}
					// this.destroy();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void setInterfaceConnectionListener(InterfaceCommand interfaceCommand) {
			this.interfaceCommand = interfaceCommand;
		}

//		Método que é chamado para chamar o método especificado por parâmetro da Classe que está implementando a interface
		private void startCommandFromInterface(String whichMethod) {
			if (this.interfaceCommand == null)
				return;
			switch (whichMethod) {

			case InterfaceCommand.ON_CONNECTED:
				this.interfaceCommand.onConnected();
				break;

			case InterfaceCommand.ON_COMMAND_RECEIVED:
				this.interfaceCommand.onCommandReceveived(stringBuilder.toString());
				break;

			case InterfaceCommand.ON_DISCONNECTED:
				this.interfaceCommand.onDisconnected();
				break;

			}
		}
	}

	private boolean passouTempoDemais() {
		long diferenca = System.currentTimeMillis() - contador;
		if (diferenca > 9000) {
			contador = System.currentTimeMillis();
			return true;
		} else {
			return false;
		}
	}

	public interface InterfaceCommand {

		public static final String ON_CONNECTED = "conexao_estabelecida";
		public static final String ON_COMMAND_RECEIVED = "comando_recebido";
		public static final String ON_DISCONNECTED = "desconectou";

		public void onConnected();

		public void onCommandReceveived(String stringRecebida);

		public void onDisconnected();

	}
}
