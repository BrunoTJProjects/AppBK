package connection;

import org.json.JSONObject;

public class ServerCommunication implements ServerConnection.InterfaceCommand {
	private String host;
	private int port;
//	private String response;
	private ServerConnection serverConnection;
	private Request request = new Request();
	private OnCommandReceived listener;
	private String login;
	private String password;

	public ServerCommunication(String host, int port, OnCommandReceived listener) {
		super();
		this.host = host;
		this.port = port;
		this.listener = listener;
		inicializarComponentes(host, port);
	}

	private boolean isCredentials() {
		return login != null && !login.isEmpty() && password != null && !password.isEmpty(); 
	}
	
	public boolean tryConnect() {
		while (!serverConnection.connect()) {
			System.out.println(">>>>tentando conexao<<<<<");
		}
		if(isCredentials()) {
			login(login, password);
		}
		return true;
	}

	public void desconectarDoServidor(boolean reconnect) {
		serverConnection.destroy(reconnect);
	}

	@Override
	public void onConnected() {
		// TODO Auto-generated method stub
		System.out.println("Conectado ao Servidor");
	}

	@Override
	public void onCommandReceveived(String stringRecebida) {
		if (stringRecebida != null && !stringRecebida.isEmpty()) {

			if (!JSONObject.isJSONValid(stringRecebida)) {
				switch (stringRecebida) {
				case "Cliente login was Successful":
					listener.onLoginRealized();
					break;
				case "Voce foi desconectado":
					listener.onLogoutRealized();
					break;
				case "ok":
					listener.onActionConfirmed();
					break;
				case "Este dispositivo nao esta conectado":
					listener.onDisconnectedDevice();
					break;
				case "no":
					listener.onTesteLogin(false);
					break;
				case "yes":
					listener.onTesteLogin(true);
					break;
				default:
					listener.onKeyReceived(stringRecebida);
				}
			} else {
				JSONObject json = new JSONObject(stringRecebida);
				if (!json.isNull("request")) {
					String request = json.getString("request");
					if(request != null && !request.isEmpty()) {
						switch (request) {
						case "setKey":
							listener.onKeyReceived(json.getString("key"), json.getString("value"));
							break;
						case "setKeys":
							listener.onKeysReceived(json.getJSONObject("keys").toString());
							break;
						}
					}
				} else {
					listener.onKeysReceived(stringRecebida);
				}
			}

		}
	}
	
	public void isLogged() {		
//		response = null;
		request.isLogged();		
//		Utilidade.contador = System.currentTimeMillis();
//		while (!Utilidade.passouTempoDemais() && response == null) {
//			try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		if (response == null)
//			return false;
//
//		if (response.equals("yes")) {
//			return true;
//		}
//			return false;
	}

	public void login(String login, String password) {
		this.login = login;
		this.password = password;
		request.requisicaoLogin(login, password);
	}
	
	private void login() {
		request.requisicaoLogin(this.login, this.password);
	}

	public void logout() {
		this.login = null;
		this.password = null;
		request.requisicaoLogout();
	}

	public void getChaves(String mac) {
		request.getChaves(mac);
	}

	public void setChaves(String mac, JSONObject chaves) {
		request.setChaves(mac, chaves);
	}

	public void getChave(String mac, String chave) {
		request.getChave(mac, chave);
	}

	public void setChave(String mac, String chave, String valor) {
		request.setChave(mac, chave, valor);
	}

	public void deleteChave(String mac, String chave) {
		request.deleteChave(mac, chave);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		System.out.println("Foi desconectado");
		System.out.println("__________________________________");
		System.out.println("Tentando reconectar");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inicializarComponentes(host, port);
		tryConnect();
	}

	private void inicializarComponentes(String host, int port) {
		if (serverConnection == null) {
			serverConnection = new ServerConnection(host, port);
			serverConnection.setInterfaceConnectionListener(this);
		}

	}

	private class Request {
		private JSONObject requisicao;
		private final String TEXT_REQ = "{" + "	\"requisicao\": {" + "		\"deviceType\": \"\","
				+ "		\"tipoReq\": \"\"," + "		\"login\": \"\"," + "		\"password\": \"\","
				+ "		\"dados\": {\r\n" + "			\"request\": \"\"," + "			\"mac\": \"\","
				+ "			\"key\": \"\"," + "			\"value\": \"\"," + "			\"keys\": {"
				+ "				\"rele1\": \"\"," + "				\"rele2\": \"\","
				+ "				\"rele3\": \"\"," + "				\"rele4\": \"\"" + "			}" + "		}"
				+ "	}" + "}";

		public Request() {
			requisicao = new JSONObject(TEXT_REQ);
		}
		
		public void isLogged() {
			resetReq();
			JSONObject req = requisicao.getJSONObject("requisicao");
			req.put("tipoReq", "is_logged_request");
			serverConnection.sendCommand(requisicao.toString());
		}

		public void requisicaoLogin(String login, String senha) {
			resetReq();
			JSONObject req = requisicao.getJSONObject("requisicao");
			req.put("deviceType", "cliente");
			req.put("tipoReq", "login_request");
			req.put("login", login);
			req.put("password", senha);
			serverConnection.sendCommand(requisicao.toString());
		}

		public void requisicaoLogout() {
			resetReq();
			JSONObject req = requisicao.getJSONObject("requisicao");
			req.put("tipoReq", "logout_request");
			serverConnection.sendCommand(requisicao.toString());
		}

		public void getChaves(String mac) {
			resetReq();
			requisicao.getJSONObject("requisicao").put("tipoReq", "command_request");
			JSONObject req = requisicao.getJSONObject("requisicao").getJSONObject("dados");
			req.put("mac", mac);
			req.put("request", "getKeys");
			serverConnection.sendCommand(requisicao.toString());
		}

		public void setChaves(String mac, JSONObject chaves) {
			resetReq();
			requisicao.getJSONObject("requisicao").put("tipoReq", "command_request");
			JSONObject req = requisicao.getJSONObject("requisicao").getJSONObject("dados");
			req.put("mac", mac);
			req.put("request", "setKeys");
			req.put("keys", chaves);
			serverConnection.sendCommand(requisicao.toString());
		}

		public void getChave(String mac, String chave) {
			resetReq();
			requisicao.getJSONObject("requisicao").put("tipoReq", "command_request");
			JSONObject req = requisicao.getJSONObject("requisicao").getJSONObject("dados");
			req.put("mac", mac);
			req.put("request", "getKey");
			req.put("key", chave);
			serverConnection.sendCommand(requisicao.toString());
		}

		public void setChave(String mac, String chave, String valor) {
			resetReq();
			requisicao.getJSONObject("requisicao").put("tipoReq", "command_request");
			JSONObject req = requisicao.getJSONObject("requisicao").getJSONObject("dados");
			req.put("mac", mac);
			req.put("request", "setKey");
			req.put("key", chave);
			req.put("value", valor);
			serverConnection.sendCommand(requisicao.toString());
		}

		public void deleteChave(String mac, String chave) {
			resetReq();
			requisicao.getJSONObject("requisicao").put("tipoReq", "command_request");
			JSONObject req = requisicao.getJSONObject("requisicao").getJSONObject("dados");
			req.put("mac", mac);
			req.put("request", "deleteKey");
			req.put("key", chave);
			serverConnection.sendCommand(requisicao.toString());
		}

		private void resetReq() {
			requisicao = null;
			requisicao = new JSONObject(TEXT_REQ);
		}

	}

	public interface OnCommandReceived {
		void onLoginRealized();

		void onTesteLogin(boolean b);

		void onDisconnectedDevice();

		void onLogoutRealized();

		void onKeyReceived(String value);
		
		void onKeyReceived(String key, String value);

		void onKeysReceived(String keys);

		void onActionConfirmed();
	}

}
