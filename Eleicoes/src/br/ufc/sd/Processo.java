package br.ufc.sd;

public class Processo implements Runnable {

	@Override
	public void run() {

		while(true) {
			System.out.println("coordenador: teste");
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
