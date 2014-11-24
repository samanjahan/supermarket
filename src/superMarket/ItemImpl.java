package superMarket;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings("serial")
public class ItemImpl extends UnicastRemoteObject implements Item{	
	String name;
	float price;
	public ItemImpl(String name, float price) throws RemoteException {
		this.name = name;
		this.price = price;
	}
	public String getName(){
		return name;
	}
	public float getPrice(){
		return price;
	}
}
