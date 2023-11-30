package Model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

import Controller.Comunicacao;
import Model.Maquina;

public class Rede {

	public static LinkedList<Maquina> maquinasRede;

	public Rede() {
		maquinasRede = new LinkedList<Maquina>();
	}
	
	public static void addMaquinaRede(Maquina maquina) {
		maquinasRede.add(maquina);
	}
	
	public static void removeMaquinaRede(Maquina maquina) {
		maquinasRede.remove(maquina);
	}
	
	public static LinkedList<Maquina> getMaquinasRede() {
		return maquinasRede;
	}
	
	public static Maquina getMaquinaCoordenadorRede() {
		for(Maquina maquina : maquinasRede) {
			if(maquina.isCoordenador()) {
				return maquina;
			}
		}
		return null;
	}
	
	public static Maquina getMaquinaLocal() throws UnknownHostException {
		InetAddress ip = InetAddress.getLocalHost();
		for(Maquina maquina : Rede.maquinasRede) {
			if(ip.getHostAddress().equals(maquina.getIpMaquina())) {
				return maquina;
			}
		}
		return null;
	}
	
	public static Maquina getMaquinaSucessora(Maquina maquinaAtual) {
		if(Rede.maquinasRede.indexOf(maquinaAtual) == Rede.maquinasRede.size()) {
			return Rede.maquinasRede.getFirst();
		} else {
			return Rede.maquinasRede.get(Rede.maquinasRede.indexOf(maquinaAtual) + 1);
		}
	}
	
	public static void defineNovoCoordenador(int identificadorNovoCoordenador) throws UnknownHostException {
		// Busca da maquina coordenadora para atualizar a Rede
		for(Maquina maquina : Rede.maquinasRede) {
			int indicePonto = maquina.getIpMaquina().lastIndexOf(".");
			String identificadorMaquina = maquina.getIpMaquina().substring(indicePonto + 1);
			if(identificadorMaquina.equals(String.valueOf(identificadorNovoCoordenador))) {
				maquina.setCoordenador(true);
				System.out.println(maquina.getIpMaquina());
				
				// Caso a propria maquina esteja se definindo como coordenadora, ela inicia as funcoes como tal
				Rede.defineFuncaoNovoCoordenador(maquina);
			} else {
				maquina.setCoordenador(false);
			}
		}
	}

	public static void defineFuncao(Maquina maquina) {
		if(Rede.getMaquinaLocal() == maquina) {
			System.out.println("Retornando para funcao normal ...");
			Comunicacao comunicacao = new Comunicacao(maquina);
		}
	}
	
	public static void defineFuncaoCoordenador(Maquina maquina) {
		if(Rede.getMaquinaLocal() == maquina) {
			System.out.println("Alterando para funcao de coordenador ...");
			Comunicacao comunicacao = new Comunicacao(maquina);
		}
	}
}
