package superMarket;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MarketImpl extends UnicastRemoteObject implements Market {
	String user;
	private List<Item> itemList = new ArrayList<Item>();
	Float price;

	protected MarketImpl(String user) throws RemoteException {		
		super();
		this.user = user;
	}

	@Override
	public List<Item> listItem() throws RemoteException {
		return itemList;
	}

	@Override
	public boolean deleteItem(String name) throws RemoteException {
		// TODO Auto-generated method stub
		if(!itemList.isEmpty()){
			for(int i = 0 ; i < itemList.size(); ++i){
				String itemName = itemList.get(i).getName().toString();
					if(itemName.equals(name)){
						itemList.remove(i);
						return true;
					}
			}
		}
		return false;
	}
	
	public synchronized boolean deleteAll(){
		if(!itemList.isEmpty()){
			itemList.clear();
			return true;
		}
		return false;
	}
	

	public Item createItem(String name, float price) throws RemoteException {
		ItemImpl item = new ItemImpl(name, price);
		itemList.add(item);
		return item;
	}
	public String getUser(){
		return user;
	}
	
	public Float getPrice(String name) throws RemoteException{
		if(!itemList.isEmpty()){
			for(int i = 0 ; i < itemList.size(); ++i){
				String itemName = itemList.get(i).getName().toString();
					if(itemName.equals(name)){
						price = itemList.get(i).getPrice();
						return price;
					}
			}
		}
		return null;
		
	}

}
