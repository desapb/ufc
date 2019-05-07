package br.ufc.sd.callback;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Cliente implements Runnable, ICliente {

	@Override
	public void atualizar(String mensagem) throws RemoteException {
		System.out.println(mensagem);
	}
	
	public static void main(String[] args) {
		Registry reg = null;
        try {
          reg = LocateRegistry.createRegistry(2000);
        }catch (Exception ex) {
        	try {
				reg = LocateRegistry.getRegistry(2000);
			} catch (RemoteException e) {
				e.printStackTrace();
				System.exit(0);
			}
        }
        Cliente c = new Cliente();
        try {
			ICliente stub = (ICliente) UnicastRemoteObject.exportObject(c, 2001);
			IServidor servidor = (IServidor) reg.lookup("servidor");
			servidor.registrar(stub);
	        new Thread(c).run();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
        

	}

	@Override
	public void run() {
		
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	

}
