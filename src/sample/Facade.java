package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Facade extends Remote{
    public String login(String username, String password) throws RemoteException;
}
