import Controller.Comunicacao;
import Model.Maquina;
import Model.Rede;

public class Main {

	public static void main(String[] args) {
		
		Maquina m1 = new Maquina("10.15.120.120", true);
		Maquina m2 = new Maquina("10.15.120.127", false);
		//Maquina m3 = new Maquina("10.15.120.132", false);
		
		Rede rede = new Rede();
		
		rede.addMaquinaRede(m1);
		rede.addMaquinaRede(m2);
		//rede.addMaquinaRede(m3);
		
		try {
			Comunicacao comunicacao = new Comunicacao(Rede.getMaquinaLocal());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
