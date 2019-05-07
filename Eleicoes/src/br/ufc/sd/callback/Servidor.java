package br.ufc.sd.callback;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class Servidor implements IServidor, Runnable {
	
	private Vector<ICliente> clientes;
	
	Servidor(){
		clientes = new Vector();
	}

	@Override
	public void registrar(ICliente cliente) throws RemoteException {
		clientes.add(cliente);	
	}

	@Override
	public void run() {
		int cont = 0;
		ICliente cl = null;
		while(true) {
			try {
				Thread.sleep(1000);
				System.out.println("Varrendo Clientes");
				cont++;				
				for (ICliente cliente : clientes) {
					cl = cliente;
					cliente.atualizar("Mensagem "+cont);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				System.out.println("Cliente "+cl+ " caiu...");
				clientes.remove(cl);
			}
		}		
	}

	public static void main(String[] args) {
		Registry reg = null;
        try {
          reg = LocateRegistry.createRegistry(1099);
        }catch (Exception ex) {
        	try {
				reg = LocateRegistry.getRegistry(1099);
			} catch (RemoteException e) {
				e.printStackTrace();
				System.exit(0);
			}
        }
        Servidor servidor = new Servidor();
        try {
			IServidor stub = (IServidor) UnicastRemoteObject.exportObject(servidor, 1100);
			reg.rebind("servidor",stub);
	        new Thread(servidor).run();	        
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
}
