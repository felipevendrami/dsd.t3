package Model;

import Controller.Comunicacao;

public class Maquina {

	private String ipMaquina;
	private boolean ativo;
	private boolean coordenador;
	
	public Maquina(String ipMaquina, boolean coordenador) {
		this.ipMaquina = ipMaquina;
		this.coordenador = coordenador;
		this.ativo = true;
	}

	public String getIpMaquina() {
		return ipMaquina;
	}

	public boolean isCoordenador() {
		return coordenador;
	}

	public void setCoordenador(boolean coordenador) {
		this.coordenador = coordenador;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}