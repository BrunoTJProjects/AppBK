package connection;

public class ComunicacaoServidor implements ConexaoBaseDados.InterfaceCommand {
	private String host;
	private int port;
	private ConexaoBaseDados conexaoBaseDados;
//	private Usuario usuario;

	public ComunicacaoServidor(String host, int port) {
		super();
		this.host = host;
		this.port = port;
		inicializarComponentes(host, port);
	}

	public boolean tryConnect() {
		while (!conexaoBaseDados.connect()) {
			System.out.println(">>>>tentando conexão<<<<<");
		}
		return true;
	}

	public void enviarComando(String comando) {
		conexaoBaseDados.sendCommand(comando);
	}

	public void desconectarDoServidor(boolean reconnect) {
		conexaoBaseDados.destroy(reconnect);
	}

	public void signInWithEmailAndPassword(String email, String password) {

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
		if (conexaoBaseDados == null) {
			conexaoBaseDados = new ConexaoBaseDados(host, port);
			conexaoBaseDados.setInterfaceConnectionListener(this);
		}

	}

}
