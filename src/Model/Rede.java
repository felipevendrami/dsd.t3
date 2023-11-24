package Model;

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
}
