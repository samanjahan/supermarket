package superMarket;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface CallBack extends Remote {
	public void notifyMe(String user, String itemName) throws RemoteException;
}
