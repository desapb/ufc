package br.ufc.sd;

import java.lang.management.ManagementFactory;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class P implements IP, Runnable{
	
	private static int PORTA = 1090;
	private static String pid;
	private static String lider;
	
	
	protected P() throws RemoteException {
		super();
		// Define o PID do processo.
		String name = ManagementFactory.getRuntimeMXBean().getName();
		pid = name.substring(0, name.indexOf('@'));
	}

	public static void main (String[] args) {
		
		System.out.println("--- INICIA PROCESSO ---");
		
		try {
	    	P processo = new P();
			IP stub = (IP) UnicastRemoteObject.exportObject(processo, getRandomPort());
			
			// Registra processo, sendo o PID a chave
			getRegistry().rebind(pid,stub);
			
			System.out.println("PID: " + pid);
			
			new Thread(processo).run();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Servico Registry RMI.
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
	
	/**
	 * Gera porta randomicamente.
	 */
	private static int getRandomPort() {
		double rand = Math.random();
		return (int)(rand * ((2999 - 2000) + 1)) + 2000;
	}
	
	/**
	 * Gera tempo de sleep randomicamente (30s a 60s).
	 */
	private static int getRandomTime() {
		double rand = Math.random();
		return (int)(rand * ((60000 - 30000) + 1)) + 3000;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				int randomTime = getRandomTime();
				
				System.out.println(+randomTime/1000 +" segundos para iniciar eleicao ----");
				Thread.sleep(randomTime);
				
				// Inicia eleicao
				this.startElection();
				
				// Verifica se o lider esta ativo
				if(!pingLider()) {
					getRegistry().unbind(lider);
					this.lider = null;
					this.startElection();
				}
		        		        
			} catch (InterruptedException | RemoteException | NotBoundException e) {
				e.printStackTrace();
			}
		}		
	}


	/**
	 * Verifica se ha processos com PID maior que o atual.	
	 * @return Lista de PIDs de processos maiores.
	 */
	private List<String> listaProcessosMaiores() {
	
		List<String> processosMaiores = new ArrayList<String>();
		
 		try {
						
			for (String p : getRegistry().list())	{
				
				if (Integer.parseInt(p) > Integer.parseInt(pid)){
	            	processosMaiores.add(p);
	            }
	        }
			
		} catch (RemoteException e) {
			System.out.println("Erro ao buscar lista de registros");
		}

 		return processosMaiores;
 		
	}

	/**
	 * Verifica se o lider esta ativo.
	 * @return true caso o lider esteja ativo, false inativo.
	 */
	private boolean pingLider() {
		if(lider == null) {
			return false;
		}
		
		try {
			IP processoLider = (IP) getRegistry().lookup(lider);
			String liderPid = processoLider.getPid();
			
			return true;
			
		} catch (RemoteException | NotBoundException e) {
			System.out.println("Erro ao se comunicar com o lider.");
			return false;
		}
			
	}
	
	@Override
	public void startElection() throws RemoteException {
		
		List<String> listaProcessos = this.listaProcessosMaiores();
		
		if(listaProcessos.size() == 0){
			System.out.println(" - inicia eleicao - ");
			if(lider == null) {
				this.setLeader(pid);
				this.notificaProcessos();
			}else {
				if(Integer.parseInt(pid) > Integer.parseInt(lider)) {
					this.setLeader(pid);
					this.notificaProcessos();
				}	
			}
		}
		
	}

	/**
	 * Notifica todos os processos sobre o novo lider.
	 * @throws RemoteException
	 */
	private void notificaProcessos() throws RemoteException {
		// Notifica demais registros sobre o novo lider
		String[] registros = getRegistry().list();
		for (String p : registros)	{
			if(!p.equals(lider)) {
				try {
					IP processo = (IP) getRegistry().lookup(p);
					processo.setLeader(lider);
				} catch (NotBoundException e) {
					System.out.println("Nao foi possivel notificar: " + p);
				}
			}
        }
	}

	@Override
	public void setLeader(String lider) throws RemoteException {
		this.lider = lider;
		System.out.println("Lider: " + lider);
	}
	
	@Override
	public String getPid() throws RemoteException {
		return this.pid;
	}

}
