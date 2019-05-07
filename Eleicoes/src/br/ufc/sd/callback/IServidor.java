package br.ufc.sd.callback;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServidor extends Remote {
	
	public void registrar(ICliente cliente) throws RemoteException;

}
