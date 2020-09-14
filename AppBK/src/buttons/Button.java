package buttons;

@SuppressWarnings("unused")
public class Button{
	
	public enum Tipo{
		SLIDE, RETETION, PULSE	
	}

	private int id;
	private Tipo tipo;
	private String nome;
	private String chave;
	private String valorInt;
	private String valorBool;
	private boolean visible;
	private int x;
	private int y;
	
	public Button() {
		super();
		this.tipo = Tipo.RETETION;
		this.visible = true;
	}

}
