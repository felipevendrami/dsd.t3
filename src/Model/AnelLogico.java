package Model;

import java.util.ArrayList;
import java.util.LinkedList;

public class AnelLogico {

	private LinkedList<Maquina> maquinasParticipantes;
	private String mensagemEleicao;

	public AnelLogico() {
		this.maquinasParticipantes = Rede.getMaquinasRede();
	}

	public LinkedList<Maquina> getMaquinasParticipantes() {
		return maquinasParticipantes;
	}

	public String getMensagemEleicao() {
		return mensagemEleicao;
	}

	public void setMensagemEleicao(String mensagemEleicao) {
		this.mensagemEleicao = mensagemEleicao;
	}
}