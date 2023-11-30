import Controller.Comunicacao;
import Model.Maquina;
import Model.Rede;

public class Main {

	public static void main(String[] args) {
		
		// Mudar Ips de acordo com as maquinas que farao parte da demonstaçao
		Maquina m1 = new Maquina("10.15.120.120", true);
		Maquina m2 = new Maquina("10.15.120.127", false);
		Maquina m3 = new Maquina("10.15.120.171", false);
		//Maquina m4 = new Maquina("10.15.120.111", false);
		//Maquina m5 = new Maquina("10.15.120.130", false);
		
		Rede rede = new Rede();
		
		rede.addMaquinaRede(m1);
		rede.addMaquinaRede(m2);
		rede.addMaquinaRede(m3);
		//rede.addMaquinaRede(m4);
		//rede.addMaquinaRede(m5);

		try {
			Comunicacao comunicacao = new Comunicacao(Rede.getMaquinaLocal());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}