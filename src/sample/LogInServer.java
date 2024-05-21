package sample;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LogInServer extends UnicastRemoteObject implements sample.Facade {

    private TreeMap clients = new TreeMap<String,String>();
    
    public LogInServer() throws RemoteException{
        super();
    }

    public static void main(String[] args) {
        try {
            Registry reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            LogInServer obj = new LogInServer();
            
            reg.rebind("rmi://localhost/service", obj);
            
            System.out.println("Server Running...");
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        }
    }


    @Override
    public String login(String username, String password) throws RemoteException {
        return null;
    }
}
