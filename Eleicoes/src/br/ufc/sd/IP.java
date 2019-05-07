package br.ufc.sd;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IP extends Remote {

	public void startElection() throws RemoteException;
	public void setLeader(String lider) throws RemoteException;
	
}
