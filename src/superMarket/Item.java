package superMarket;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface Item extends Remote{
	public String getName() throws RemoteException;
	public float getPrice() throws RemoteException;
}
