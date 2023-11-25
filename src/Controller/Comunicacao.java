package Controller;

import java.util.ArrayList;

import Connection.Conexao;
import Model.Maquina;
import Model.Rede;

public class Comunicacao{

	// Tempos para execução da Thread
	private int REALIZAR_COMUNICACAO = 5000;
	private int INATIVAR_LIDER = 17000;
	
	private Rede rede;
	private Maquina maquina;
	private boolean emEleicao = false;

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
				Thread.sleep(INATIVAR_LIDER);
				this.maquina.setAtivo(false);
				System.out.println("LÍDER INATIVO");
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
				while (this.maquina.isAtivo()) {
					verificaEleicaoEmAndamento();
					Thread.sleep(REALIZAR_COMUNICACAO);
					conexao.fazerRequisicao(maquina, Rede.getMaquinaLiderRede());
				}
			} catch (Exception e) {
				//System.out.println(e.getMessage());
				// Máquina percebe que o líder caiu e inicia uma nova eleição se já não houver uma
				if(!emEleicao) {
					System.out.println("ATENÇÃO: Sem resposta do líder, iniciando eleição ...");
					Eleicao eleicao = new Eleicao(this.maquina);
					this.emEleicao = true;
					eleicao.iniciarEleicao();
				}
			}
		}).start();
	}
	
	public void verificaEleicaoEmAndamento() {
		new Thread(() -> {
			Conexao conexao = new Conexao();
			conexao.aguardaMensgemEleicao(this.maquina);
		}).start();
	}
}