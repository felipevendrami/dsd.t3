package Controller;

import java.util.ArrayList;

import Connection.Conexao;
import Model.AnelLogico;
import Model.Maquina;

public class Eleicao {

	// Maquina que iniciou a eleicao
	private Maquina maquinaEleicao;
	private int idMaquinaEleita = 0;
	private AnelLogico anelLogico;
	private String[] mensagemEleicao;

	public Eleicao(Maquina maquinaEleicao) {
		this.maquinaEleicao = maquinaEleicao;
		this.anelLogico = new AnelLogico();
	}

	public int getIdMaquinaEleita() {
		return idMaquinaEleita;
	}

	public void iniciarEleicao() {
		/*
		 * Iniciamos a eleicao com array de 2 posicoes, onde : 
		 * [0] eh a operacao (ELEICAO ou COORDENADOR)
		 * [1] sao os identificadores envolvidos
		 */
		this.mensagemEleicao = new String[2];
		this.mensagemEleicao[0] = "ELEICAO";
		this.mensagemEleicao[1] = getIdentificadorMaquina();
		enviarMensagemEleicao();
	}

	private String getIdentificadorMaquina() {
		// Método que retorna o identificador da maquina (ultimos digitos)
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
			// Verificamos se a maquina atual participa da eleicao
			
			for(String posicao : this.mensagemEleicao) {
				if(posicao.equals(getIdentificadorMaquina())) {
					// TODO: montar a mensagem de coordenados
					System.out.println("Percorreu o anel, mensagem: " + mensagemRecebida);
				}
			}
			if(this.mensagemEleicao[0].equals("ELEICAO")) {
				// Adicionamos o identificador da maquina
				this.mensagemEleicao[1] += getIdentificadorMaquina();
			} else {
				// TODO: caso a mensagem for de coordenados
			}
			enviarMensagemEleicao();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void defineNovoCoordenador() {
		
	}
}
