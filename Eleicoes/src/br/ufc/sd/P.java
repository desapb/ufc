package br.ufc.sd;

import java.lang.management.ManagementFactory;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class P implements IP, Runnable{
	
	private static int PORTA = 1090;

	protected P() throws RemoteException {
		super();
	}

	public static void main (String[] args) {
		System.out.println("--- CRIA PROCESSO ---");
		try {
	    	P processo = new P();
			IP stub = (IP) UnicastRemoteObject.exportObject(processo, getRandomPort());
			getRegistry().rebind(ManagementFactory.getRuntimeMXBean().getName(),stub);
			new Thread(processo).run();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gera porta randomicamente.
	 */
	private static int getRandomPort() {
		double rand = Math.random();
		return (int)(rand * ((2999 - 2000) + 1)) + 2000;
	}
	
	/**
	 * Gera tempo randomicamente (30s a 60s).
	 */
	private static int getRandomTime() {
		double rand = Math.random();
		return (int)(rand * ((60000 - 30000) + 1)) + 3000;
	}
	
	/**
	 * Inicia servico RMI.
	 */
	private static Registry getRegistry() {
		Registry reg = null;
	    try {
	      reg = LocateRegistry.createRegistry(PORTA);
	    }catch (Exception ex) {
	    	try {
				reg = LocateRegistry.getRegistry(PORTA);
			} catch (RemoteException e) {
				e.printStackTrace();
				System.exit(0);
			}
	    }
	    
	    return reg;
	}
	
	
	
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(getRandomTime());
				this.startElection();
		        
				// Imprime processos registrados
				//String[] boundNames = getRegistry().list();
		        //for(int i = 0; i < boundNames.length; i++) {
		        //	System.out.println("Clientes - " + i);
		        //}
		        
		        // 
		        
			} catch (InterruptedException | RemoteException e) {
				e.printStackTrace();
			}
		}		
	}


	@Override
	public void startElection() throws RemoteException {
		System.out.println("inicia eleicao");
	}


	@Override
	public void setLeader() throws RemoteException {
		System.out.println("seta lider");
		
	}
	
	

}
