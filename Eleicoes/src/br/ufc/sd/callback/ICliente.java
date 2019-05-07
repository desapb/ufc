package br.ufc.sd.callback;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICliente extends Remote{
	
	public void atualizar(String mensagem) throws RemoteException;

}