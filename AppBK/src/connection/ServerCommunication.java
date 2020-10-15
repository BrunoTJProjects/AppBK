package connection;

import org.json.JSONObject;

public class ServerCommunication implements ServerConnection.InterfaceCommand {
	private String host;
	private int port;
	private ServerConnection serverConnection;
	private Request request = new Request();
//	private Usuario usuario;

	public ServerCommunication(String host, int port) {
		super();
		this.host = host;
		this.port = port;
		inicializarComponentes(host, port);
	}

	public boolean tryConnect() {
		while (!serverConnection.connect()) {
			System.out.println(">>>>tentando conexão<<<<<");
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
		System.out.println("Comando recebido: " + stringRecebida);
	}
	
	public void login() {
		request.requisicaoLogin("bruno.melo@tcm10.com.br", "8aB1yGj4");
	}
	
	public void getChaves() {
		request.getChaves();
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
		@SuppressWarnings("unused")
		private JSONObject requisicao;
		private String textReq = "{" + 
				"	\"requisicao\": {" + 
				"		\"deviceType\": \"\"," + 
				"		\"tipoReq\": \"\"," + 
				"		\"login\": \"\"," + 
				"		\"password\": \"\"," + 
				"		\"dados\": {\r\n" + 
				"			\"request\": \"\"," + 
				"			\"mac\": \"\"," + 
				"			\"key\": \"\"," + 
				"			\"value\": \"\"," + 
				"			\"keys\": {" + 
				"				\"rele1\": \"\"," + 
				"				\"rele2\": \"\"," + 
				"				\"rele3\": \"\"," + 
				"				\"rele4\": \"\"" + 
				"			}" + 
				"		}" + 
				"	}" + 
				"}";


		public Request() {
			requisicao = new JSONObject(textReq);
		}
		
		public void requisicaoLogin(String login, String senha) {
			requisicao = null;
			requisicao = new JSONObject(textReq);
			JSONObject req = requisicao.getJSONObject("requisicao");
			req.put("deviceType","cliente");
			req.put("tipoReq", "login_request");
			req.put("login", login);
			req.put("password", senha);
			serverConnection.sendCommand(requisicao.toString());
		}
		
		public void getChaves() {
			requisicao = null;
			requisicao = new JSONObject(textReq);
			requisicao.getJSONObject("requisicao").put("tipoReq", "command_request");
			JSONObject req = requisicao.getJSONObject("requisicao").getJSONObject("dados");
			req.put("mac","11-22-33-44-55-66");
			req.put("request", "getKeys");
			serverConnection.sendCommand(requisicao.toString());
		}

	}

}
