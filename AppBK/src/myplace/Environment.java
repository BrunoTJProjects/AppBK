package myplace;

import java.net.URI;
import java.util.List;

import buttons.Button;
import buttons.Cena;

@SuppressWarnings("unused")
public class Environment {
	
	private String nome;
	private URI uri;
	private Tipo tipo;
	private List<Button> botoes;
	private List<Cena> cenas;
	
	public enum Tipo{
		SALA, BANHEIRO, COZINHA, AREA_EXTERNA, QUARTO, SALA_DE_ESTAR, SALA_DE_JANTAR, AREA_SERVICO 	
	}
	
	
}
