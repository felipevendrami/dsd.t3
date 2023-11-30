package Controller;

import Connection.Conexao;
import Model.Maquina;
import Model.Rede;

public class Comunicacao{

	// Tempos para execucao da Thread
	private int REALIZAR_COMUNICACAO = 5000;
	private int INATIVAR_COORDENADOR = 17000;
	private int ATIVAR_MAQUINA = 10000;
	
	private Rede rede;
	private Maquina maquina;
	private Eleicao eleicao;

	public Comunicacao(Maquina maquina){
		this.maquina = maquina;
		
		// Definir qual o papel da maquina caso for coordenador ou nao
		if(maquina.isCoordenador()) {
			operacaoMaquinaCoordenador();
		} else {
			operacaoMaquina();
			verificaEleicaoEmAndamento();
		}
	}

	public void operacaoMaquinaCoordenador() {
		// Como coordenador, a maquina aguarda as requisiçoes das outras maquinas e responde
		new Thread(() -> {
			try {
				Conexao conexao = new Conexao();
				conexao.aguardarConexoes(this.maquina);
				// Inativamos o coordenador para forçar a realizacao de uma nova eleicao
				Thread.sleep(INATIVAR_COORDENADOR);
				this.maquina.setAtivo(false);
				this.maquina.setCoordenador(false);
				System.out.println("COORDENADOR INATIVO");
				/*Thread.sleep(ATIVAR_MAQUINA);
				verificaEleicaoEmAndamento();
				this.maquina.setAtivo(true);*/
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}).start();
	}
	
	public void operacaoMaquina() {
		// Como maquina normal, faz requisicoes ao coordenador e recebe a resposta
		System.out.println("Maquina " + this.maquina.getIpMaquina() + " disponivel na rede.");
		new Thread(() -> {
			try {
				Conexao conexao = new Conexao();
				while ((this.maquina.isAtivo()) && (this.eleicao == null)) {
					Thread.sleep(REALIZAR_COMUNICACAO);
					if(this.eleicao == null) {
						conexao.fazerRequisicao(maquina, Rede.getMaquinaCoordenadorRede());
					}
				}
			} catch (Exception e) {
				//System.out.println(e.getMessage());
				// Maquina percebe que o coordenador caiu e inicia uma nova eleicao se ja nao houver uma
				if(this.eleicao == null) {
					System.out.println("ATENCAO: Sem resposta do coordenador, iniciando eleicao ...");
					this.eleicao = new Eleicao(this.maquina);
					eleicao.iniciarEleicao();
				}
			}
		}).start();
	}
	
	public void verificaEleicaoEmAndamento(){
		new Thread(() -> {
			while(!this.maquina.isCoordenador()) {
				// Criamos um processo para receber mensagem de eleicao
				Conexao conexao = new Conexao();
				String mensagemRecebida = conexao.aguardaMensgemEleicao(this.maquina);
				// Define nova Eleicao caso nao haja e processa a mensagem recebida
				if(this.maquina.isAtivo()) {
					if(this.eleicao == null) {
						System.out.println("Processo de eleicao em andamento...");
						this.eleicao = new Eleicao(this.maquina);
					}
					this.eleicao.recebeMensagemEleicao(mensagemRecebida);
				} else {
					break;
				}
			}
		}).start();
	}
}