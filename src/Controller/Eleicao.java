package Controller;

import java.util.ArrayList;

import Connection.Conexao;
import Model.AnelLogico;
import Model.Maquina;
import Model.Rede;

public class Eleicao {

	// Maquina que iniciou a eleicao
	private Maquina maquinaEleicao;
	private String identificadorMaquina;
	private int novoCoordenador = 0;
	private AnelLogico anelLogico;
	private String[] mensagemEleicao;

	public Eleicao(Maquina maquinaEleicao) {
		this.maquinaEleicao = maquinaEleicao;
		this.anelLogico = new AnelLogico();
		this.identificadorMaquina = getIdentificadorMaquina();
	}

	public void iniciarEleicao() {
		/*
		 * Iniciamos a eleicao com array de 2 posicoes, onde : 
		 * [0] eh a operacao (ELEICAO ou COORDENADOR)
		 * [1] sao os identificadores envolvidos
		 */
		this.mensagemEleicao = new String[2];
		this.mensagemEleicao[0] = "ELEICAO";
		this.mensagemEleicao[1] = this.identificadorMaquina;
		enviarMensagemEleicao();
	}

	private String getIdentificadorMaquina() {
		// Metodo que retorna o identificador da maquina (ultimos digitos)
		int indicePonto = this.maquinaEleicao.getIpMaquina().lastIndexOf(".");
		return this.maquinaEleicao.getIpMaquina().substring(indicePonto + 1);
	}

	public void enviarMensagemEleicao() {
		try {
			// Montamos a mensagem de eleicao como uma String para enviar para a maquina sucessora
			String mensagemEnvio = String.join(",", this.mensagemEleicao);
			Conexao conexao = new Conexao();
			conexao.enviaMensagemEleicao(mensagemEnvio, this.anelLogico.getMaquinaSucessora(this.maquinaEleicao));
		} catch (Exception e) {
			// Em caso de erro de comunicacao com maquina sucessora, enviamos para a proxima maquina
			System.out.println("Procurando proxima maquina ...");
			// TODO: relizar envio para máquina sucessora
		}
	}

	public void recebeMensagemEleicao(String mensagemRecebida) {
		try {
			// Montamos a mensagem em um array para processarmos
			this.mensagemEleicao = mensagemRecebida.split(",");
			
			// Verificamos se a mensagem eh de ELEICAO ou COORDENADOR
			if(this.mensagemEleicao[0].equals("ELEICAO")) {
				//Identifica se a maquina ja esta participando
				if(maquinaParticipante()) {
					// Se sim, define o coordenador e monta a mensagem de COORDENADOR
					defineNovoCoordenador();
					this.mensagemEleicao[0] = "COORDENADOR";
					this.mensagemEleicao[1] = String.valueOf(this.novoCoordenador);
				} else {
					// Se nao, adicionamos o identificador da maquina
					if(this.maquinaEleicao.isAtivo()) {
						this.mensagemEleicao[1] += "," + this.mensagemEleicao;
						enviarMensagemEleicao();
					}
				}
			} else {
				// Se a mensagem for de COORDENADOR, atualizamos a Rede com o novo coordenador
				System.out.print("Definindo novo coodenador: ");
				this.novoCoordenador = Integer.parseInt(this.mensagemEleicao[1]);
				Rede.defineNovoCoordenador(this.novoCoordenador);
			}
			if(this.novoCoordenador == 0) {
				enviarMensagemEleicao();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void defineNovoCoordenador() {
		String[] participantes = this.mensagemEleicao[1].split(",");
		for(String identificador : participantes) {
			int identificadorInt = Integer.parseInt(identificador);
			if(identificadorInt > this.novoCoordenador) {
				this.novoCoordenador = identificadorInt;
			}
		}
	}
	
	private boolean maquinaParticipante() {
		if(this.mensagemEleicao[1].contains(this.identificadorMaquina)) {
			return true;
		} else {
			return false;
		}
	}
}
