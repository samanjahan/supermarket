package superMarket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Marketplace extends Remote {
	public Market newMarket(String name) throws RemoteException;
	public boolean deleteMarket(String name) throws RemoteException;
	public Market getMarket (String name) throws RemoteException;
	public String[] listMarket() throws RemoteException;
	public String[] listAllMarket() throws RemoteException;
	public ArrayList<String> listAllItem() throws RemoteException;
	public void wish (String name , String price , CallBack client) throws RemoteException;
	public void chechWish() throws RemoteException;
	public String listMyItem(String userName) throws RemoteException;
	public boolean buy (String itemName, String userWillBuy ,String userWillSell) throws RemoteException;
}
