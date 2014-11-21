package superMarket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Market extends Remote {
	public List<Item> listItem() throws RemoteException;
	public boolean deleteItem(String name) throws RemoteException;
	public Item createItem(String name,float price) throws RemoteException;
	public boolean deleteAll() throws RemoteException;
}