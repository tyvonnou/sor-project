package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServeurRMI extends Remote {
	public String meth() throws RemoteException;
}
