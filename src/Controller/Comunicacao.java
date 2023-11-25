package Controller;

import java.util.ArrayList;

import Connection.Conexao;
import Model.Maquina;
import Model.Rede;

public class Comunicacao{

	// Tempos para execução da Thread
	private int REALIZAR_COMUNICACAO = 5000;
	
	private Rede rede;
	private Maquina maquina;

	public Comunicacao(Maquina maquina){
		this.maquina = maquina;
		
		// Definir qual o papel da máquina caso for líder ou não
		if(maquina.isLider()) {
			operacaoMaquinaLider();
		} else {
			operacaoMaquina();
		}
	}

	public void operacaoMaquinaLider() {
		// Como líder, a máquina aguarda as requisições das outras máquinas e responde
		new Thread(() -> {
			try {
				Conexao conexao = new Conexao();
				conexao.aguardarConexoes(this.maquina);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}).start();
	}
	
	public void operacaoMaquina() {
		// Como máquina normal, faz requisições ao líder e recebe a resposta
		new Thread(() -> {
			try {
				Conexao conexao = new Conexao();
				while (true) {
					Thread.sleep(REALIZAR_COMUNICACAO);
					conexao.fazerRequisicao(maquina, Rede.getMaquinaLiderRede());
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}).start();
	}
	
	public void enviarMensagemEleicao(Maquina maquina, ArrayList<Integer> mensagemEleicao) {
		try {
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void recebeMensagemEleicao(ArrayList<Integer> mensagemEleicao) {
		try {
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
