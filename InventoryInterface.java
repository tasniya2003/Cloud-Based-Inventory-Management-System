package common;

import java.rmi.*;
import java.util.*;

public interface InventoryInterface extends Remote {
    String addProduct(String name, int quantity, double price) throws RemoteException;
    String updateProduct(int id, int quantity) throws RemoteException;
    String deleteProduct(int id) throws RemoteException; 
    List<String> viewProducts() throws RemoteException;
}
