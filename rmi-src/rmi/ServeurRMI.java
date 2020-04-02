package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServeurRMI extends Remote {
	public String meth() throws RemoteException;
	public void newImage(String title, Long size) throws RemoteException;
	public void insertByte(String title, Long begin, byte[] buffer) throws RemoteException;
}
