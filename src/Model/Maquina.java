package Model;

public class Maquina {

	private String ipMaquina;
	private boolean ativo;
	private boolean lider;
	
	public Maquina(String ipMaquina, boolean lider) {
		this.ipMaquina = ipMaquina;
		this.lider = lider;
		this.ativo = true;
	}

	public String getIpMaquina() {
		return ipMaquina;
	}

	public boolean isLider() {
		return lider;
	}

	public void setLider(boolean lider) {
		this.lider = lider;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}