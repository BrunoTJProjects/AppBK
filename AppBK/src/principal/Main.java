package principal;

import java.util.Scanner;

import org.json.JSONObject;

import connection.ServerCommunication;
import connection.ServerCommunication.OnCommandReceived;

public class Main {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws InterruptedException {

		ServerCommunication serverCommunication = new ServerCommunication("179.107.244.81", 58365,
				new OnCommandReceived() {

					@Override
					public void onCommandReveived(String comando) {
						System.out.println(comando);

					}
				});

		serverCommunication.tryConnect();

		Scanner scan = new Scanner(System.in);
		String comando = "";

		while (!comando.equals("fim")) {
			comando = scan.nextLine();
			switch (comando) {
			case "login":
				System.out.println(serverCommunication.login("bruno.melo@tcm10.com.br", "8aB1yGj4"));
				break;
			case "logout":
				System.out.println(serverCommunication.logout());
				break;
			case "getChaves":
				System.out.println(serverCommunication.getChaves("11:22:33:44:55:66"));
				break;
			case "setChaves":
				System.out.println(
						serverCommunication.setChaves("11:22:33:44:55:66", new JSONObject("{\"rele1\":\"1\"}")));
				break;
			case "getChave":
				System.out.println(serverCommunication.getChave("11:22:33:44:55:66", "rele1"));
				break;
			case "setChave":
				System.out.println(serverCommunication.setChave("11:22:33:44:55:66", "rele1", "0"));
				break;
			case "deleteChave":
				System.out.println(serverCommunication.deleteChave("11:22:33:44:55:66", "rele1"));
				break;
			}
		}
	}

//		comunicacaoServidor.desconectarDoServidor(true);

	/*
	 * Cliente cliente = new Cliente("Glaucio Guerra", 'M', "0000000001"); try {
	 * //Gera o arquivo para armazenar o objeto FileOutputStream arquivoGrav = new
	 * FileOutputStream("saida.dat"); //Classe responsavel por inserir os objetos
	 * ObjectOutputStream objGravar = new ObjectOutputStream(arquivoGrav); //Grava o
	 * objeto cliente no arquivo objGravar.writeObject(cliente); objGravar.flush();
	 * objGravar.close(); arquivoGrav.flush(); arquivoGrav.close();
	 * System.out.println("Objeto gravado com sucesso!"); }
	 * 
	 * catch(Exception e) { e.printStackTrace(); }
	 * System.out.println("Recuperando objeto: "); try { //Carrega o arquivo
	 * FileInputStream arquivoLeitura = new FileInputStream("saida.dat"); // Classe
	 * responsavel por recuperar os objetos do arquivo ObjectInputStream objLeitura
	 * = new ObjectInputStream(arquivoLeitura);
	 * 
	 * // Cliente cli = (Cliente) objLeitura.readObject(); //
	 * System.out.println(cli.getNome());
	 * 
	 * // Cliente cli = getObject(Cliente.class, objLeitura); //
	 * System.out.println(cli);
	 * 
	 * Cliente cli = getObject(objLeitura);
	 * 
	 * System.out.println(cliente.getCpf());
	 * 
	 * objLeitura.close(); arquivoLeitura.close(); } catch(Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * 
	 * 
	 * }
	 * 
	 * public static <Tipo> Tipo getObject(Class<Tipo> classe, ObjectInputStream ob)
	 * throws ClassNotFoundException, IOException { return
	 * classe.cast(ob.readObject()); }
	 * 
	 * 
	 * public static <T> T getObject(ObjectInputStream ob) throws
	 * ClassNotFoundException, IOException { return (T) ob.readObject(); }
	 */

}
