package br.ufc.sd;

import java.lang.management.ManagementFactory;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class P implements IP, Runnable{
	
	private static int PORTA = 1090;
	private static String pid;
	private static String lider;
	

	
	protected P() throws RemoteException {
		super();
		String name = ManagementFactory.getRuntimeMXBean().getName();
		pid = name.substring(0, name.indexOf('@'));
	}

	public static void main (String[] args) {
		
		System.out.println("--- CRIA PROCESSO ---");
		
		try {
	    	P processo = new P();
			IP stub = (IP) UnicastRemoteObject.exportObject(processo, getRandomPort());
			getRegistry().rebind(pid,stub);
			
			System.out.println("PID: " + pid);
			
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
				int randomTime = getRandomTime();
				
				System.out.println(+randomTime/1000 +" segundos para iniciar eleicao ----");
				Thread.sleep(randomTime);
				
				this.startElection();
		        		        
			} catch (InterruptedException | RemoteException e) {
				e.printStackTrace();
			}
		}		
	}


	@Override
	public void startElection() throws RemoteException {
		System.out.println(" - inicia eleicao - ");
		
		String[] boundNames = getRegistry().list();
		
		for (String p : boundNames)	{
			
			if(lider == null){
				lider = pid;
			}
			
			if (Integer.parseInt(p) > Integer.parseInt(lider)){
            	lider = p;
            }
        }
		
		this.setLeader(lider);
	}

	@Override
	public void setLeader(String lider) throws RemoteException {
		System.out.println("Lider: " + lider);
	}
	
	

}
