package Controller;

import java.util.ArrayList;

import Model.AnelLogico;
import Model.Maquina;

public class Eleicao {

	// Máquina que iniciou a eleição
	private Maquina maquinaEleicao;
	private int idMaquinaEleita = 0;
	private AnelLogico anelLogico;
	
	public Eleicao(Maquina maquinaEleicao) {
		this.maquinaEleicao = maquinaEleicao;
		this.anelLogico = new AnelLogico();
	}
	
	public int getIdMaquinaEleita() {
		return idMaquinaEleita;
	}

	public void iniciarEleicao() {
		new Thread(() -> {
			
		});
	}
	
	public void enviarMensagemEleicao() {
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
	
	public void defineNovoCoordenador() {
		/*for(int valor : this.anelLogico.getMensagemEleicao()) {
			if(valor > this.idMaquinaEleita) {
				this.idMaquinaEleita = valor;
			}
		}*/
	}
}
