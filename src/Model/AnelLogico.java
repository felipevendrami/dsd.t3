package Model;

import java.util.ArrayList;
import java.util.LinkedList;

public class AnelLogico {

	private LinkedList<Maquina> maquinasParticipantes;
	private ArrayList<Integer> mensagemEleicao;

	public AnelLogico(LinkedList<Maquina> maquinasRede) {
		this.maquinasParticipantes = maquinasRede;
		this.mensagemEleicao = new ArrayList<>();
	}

	public LinkedList<Maquina> getMaquinasParticipantes() {
		return maquinasParticipantes;
	}

	public ArrayList<Integer> getMensagemEleicao() {
		return mensagemEleicao;
	}
	
	public void addIpMensagemEleicao(int ip) {
		this.mensagemEleicao.add(ip);
	}
}