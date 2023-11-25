package Model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

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
	
	public static Maquina getMaquinaLiderRede() {
		for(Maquina maquina : maquinasRede) {
			if(maquina.isLider()) {
				return maquina;
			}
		}
		return null;
	}
	
	public static Maquina getMaquinaLocal() throws UnknownHostException {
		InetAddress ip = InetAddress.getLocalHost();
		for(Maquina maquina : Rede.maquinasRede) {
			if(maquina.getIpMaquina() == ip.getHostAddress()) {
				return maquina;
			}
		}
		return null;
	}
}
