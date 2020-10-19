package utils;

public class Utilidade {
	
	
	public static long contador;

	public synchronized static boolean passouTempoDemais() {
		long diferenca = System.currentTimeMillis() - contador;
		if (diferenca > 5000) {
			contador = System.currentTimeMillis();
			return true;
		} else {
			return false;
		}
	}
}
