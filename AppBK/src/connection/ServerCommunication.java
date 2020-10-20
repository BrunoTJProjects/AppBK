package connection;

import org.json.JSONObject;

import utils.Utilidade;

public class ServerCommunication implements ServerConnection.InterfaceCommand {
	private String host;
	private int port;
	private String response;
	private ServerConnection serverConnection;
	private Request request = new Request();
	private OnCommandReceived listener;

	public ServerCommunication(String host, int port, OnCommandReceived listener) {
		super();
		this.host = host;
		this.port = port;
		this.listener = listener;
		inicializarComponentes(host, port);
	}

	public boolean tryConnect() {
		while (!serverConnection.connect()) {
			System.out.println(">>>>tentando conex�o<<<<<");
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
		response = stringRecebida;
		if(JSONObject.isJSONValid(stringRecebida)) {
			JSONObject json = new JSONObject(stringRecebida);
			if(!json.isNull("request"))
			listener.onCommandReveived(stringRecebida);
		}		
//		System.out.println("Comando recebido: " + stringRecebida);
	}

	public boolean login(String login, String password) {
		response = null;
		request.requisicaoLogin(login, password);
		Utilidade.contador = System.currentTimeMillis();
		while (!Utilidade.passouTempoDemais() && response == null) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (response == null)
			return false;

		if (response.equals("Cliente login was Successful"))
			return true;

		return false;
	}

	public boolean logout() {
		response = null;
		request.requisicaoLogout();
		Utilidade.contador = System.currentTimeMillis();
		while (!Utilidade.passouTempoDemais() && response == null) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (response == null)
			return false;

		if (response.equals("Voc� foi desconectado"))
			return true;

		return false;

	}

	public String getChaves(String mac) {
		response = null;
		request.getChaves(mac);
		Utilidade.contador = System.currentTimeMillis();
		while (!Utilidade.passouTempoDemais() && response == null) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return response;
	}

	public boolean setChaves(String mac, JSONObject chaves) {
		response = null;
		request.setChaves(mac, chaves);
		Utilidade.contador = System.currentTimeMillis();
		while (!Utilidade.passouTempoDemais() && response == null) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (response == null)
			return false;

		if (response.equals("ok"))
			return true;

		return false;

	}

	public String getChave(String mac, String chave) {
		response = null;
		request.getChave(mac, chave);
		Utilidade.contador = System.currentTimeMillis();
		while (!Utilidade.passouTempoDemais() && response == null) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return response;

	}

	public boolean setChave(String mac, String chave, String valor) {
		response = null;
		request.setChave(mac, chave, valor);
		Utilidade.contador = System.currentTimeMillis();
		while (!Utilidade.passouTempoDemais() && response == null) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (response == null)
			return false;

		if (response.equals("ok"))
			return true;

		return false;
	}

	public boolean deleteChave(String mac, String chave) {
		response = null;		
		request.deleteChave(mac, chave);
		Utilidade.contador = System.currentTimeMillis();
		while (!Utilidade.passouTempoDemais() && response == null) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (response == null)
			return false;

		if (response.equals("ok"))
			return true;

		return false;
		

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
		private String textReq = "{" + "	\"requisicao\": {" + "		\"deviceType\": \"\","
				+ "		\"tipoReq\": \"\"," + "		\"login\": \"\"," + "		\"password\": \"\","
				+ "		\"dados\": {\r\n" + "			\"request\": \"\"," + "			\"mac\": \"\","
				+ "			\"key\": \"\"," + "			\"value\": \"\"," + "			\"keys\": {"
				+ "				\"rele1\": \"\"," + "				\"rele2\": \"\","
				+ "				\"rele3\": \"\"," + "				\"rele4\": \"\"" + "			}" + "		}"
				+ "	}" + "}";

		public Request() {
			requisicao = new JSONObject(textReq);
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
			requisicao = new JSONObject(textReq);
		}

	}
	
	public interface OnCommandReceived{
		void onCommandReveived(String comando);
	}

}
