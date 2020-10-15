package principal;

import java.util.Scanner;

import connection.ServerCommunication;

public class Main {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws InterruptedException {

		ServerCommunication serverCommunication = new ServerCommunication("179.107.244.81", 58365);

		System.out.println(serverCommunication.tryConnect());

		Scanner scan = new Scanner(System.in);
		String comando = "";
		Thread.sleep(8000);
		
		
		serverCommunication.login();
		
		
		while(!comando.equals("fim")) {
			comando = scan.nextLine();
			
			serverCommunication.getChaves();
		}		
		
//		comunicacaoServidor.desconectarDoServidor(true);
		
		
		
		
		
		
		
		
		
		
		
		
	/*	Cliente cliente = new Cliente("Glaucio Guerra", 'M', "0000000001");
	    try
	    {
	      //Gera o arquivo para armazenar o objeto
	      FileOutputStream arquivoGrav =
	      new FileOutputStream("saida.dat");
	      //Classe responsavel por inserir os objetos
	      ObjectOutputStream objGravar = new ObjectOutputStream(arquivoGrav);
	      //Grava o objeto cliente no arquivo
	      objGravar.writeObject(cliente);
	      objGravar.flush();
	      objGravar.close();
	      arquivoGrav.flush();
	      arquivoGrav.close();
	      System.out.println("Objeto gravado com sucesso!");
	    }

	    catch(Exception e) {
	      e.printStackTrace();
	    }
	    System.out.println("Recuperando objeto: ");
	    try
	    {
	      //Carrega o arquivo
	      FileInputStream arquivoLeitura = new FileInputStream("saida.dat");
	           // Classe responsavel por recuperar os objetos do arquivo
	      ObjectInputStream objLeitura =
	      new ObjectInputStream(arquivoLeitura);
	      
//	      Cliente cli = (Cliente) objLeitura.readObject();
//	      System.out.println(cli.getNome());
	      
//	      Cliente cli = getObject(Cliente.class, objLeitura);
//	      System.out.println(cli);
	      
	      Cliente cli = getObject(objLeitura);
	      
	      System.out.println(cliente.getCpf());
	      
	      objLeitura.close();
	      arquivoLeitura.close();
	    }
	    catch(Exception e) {
	      e.printStackTrace();
	    }
		
		
		
	}

	public static <Tipo> Tipo getObject(Class<Tipo> classe, ObjectInputStream ob)
			throws ClassNotFoundException, IOException {
		return classe.cast(ob.readObject());
	}


	public static <T> T getObject(ObjectInputStream ob) throws ClassNotFoundException, IOException {
		return (T) ob.readObject();
	}*/
		
		
		
		
		
		

	}

}
