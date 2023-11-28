package Model;

import java.util.ArrayList;
import java.util.LinkedList;

public class AnelLogico {

	private LinkedList<Maquina> anelLogico;
	private String mensagemEleicao;

	public AnelLogico() {
		this.anelLogico = Rede.getMaquinasRede();
	}

	public LinkedList<Maquina> getAnelLogico() {
		return anelLogico;
	}

	public String getMensagemEleicao() {
		return mensagemEleicao;
	}

	public void setMensagemEleicao(String mensagemEleicao) {
		this.mensagemEleicao = mensagemEleicao;
	}
	
	public Maquina getMaquinaSucessora(Maquina maquinaAtual) {
		if(this.anelLogico.indexOf(maquinaAtual) == this.anelLogico.size()) {
			return this.anelLogico.getFirst();
		} else {
			return this.anelLogico.get(this.anelLogico.indexOf(maquinaAtual) + 1);
		}
	}
}