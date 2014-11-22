package superMarket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Bank.RejectedException;

@SuppressWarnings("serial")
public class MarketplaceImpl extends UnicastRemoteObject implements Marketplace {
	private  CallBack client;
	private String marketPlaceName;
	private Map<String, Market> markets = new HashMap<String, Market>();
	private Map<String,CallBack> wisheList = new HashMap<String, CallBack>();

	protected MarketplaceImpl(String marketPlaceName) throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		this.marketPlaceName = marketPlaceName;
	}

	@Override
	public synchronized Market newMarket(String name) throws RemoteException {
		// TODO Auto-generated method stub
		MarketImpl marketImpl = (MarketImpl) markets.get(name);
		if(marketImpl != null){
			System.out.println("Market [" + name + "] exists!!!");
			throw new RejectedException("Rejected: Lockalhost.SuperMarket: "
					+ marketPlaceName + " Market for: " + name + " already exists: "
					+ marketImpl);
		}
		marketImpl = new MarketImpl(name);
		markets.put(name, marketImpl);
		System.out.println("The " + marketImpl + "has been created for " + name);
	//	chechWish();
		return marketImpl;
	}

	@Override
	public boolean deleteMarket(String name) throws RemoteException {
		// TODO Auto-generated method stub
		if (!hasMarket(name)) {
			return false;
		}
		
		Market market = markets.get(name);
		if(!market.listItem().isEmpty()){		
			market.deleteAll();
		}
		
		markets.remove(name);
		System.out.println("The " + name + " has been deleted!");
		return true;
	}

	@Override
	public synchronized Market getMarket(String name) throws RemoteException {
		// TODO Auto-generated method stub
		return markets.get(name);
	}

	@Override
	public synchronized String[] listMarket(){
		// TODO Auto-generated method stub
		return markets.keySet().toArray(new String[1]);
	}
	
	private boolean hasMarket(String name) {
		return markets.get(name) != null;
	}

	@Override
	public ArrayList<String> listAllItem() throws RemoteException {
		ArrayList<String> items = new ArrayList<String>();
		
		for(Market marketHolder : markets.values().toArray( new Market[0])){
			
			ArrayList<Item> tempItems = (ArrayList<Item>)marketHolder.listItem();
			
			for(int i = 0; i < tempItems.size(); i++){
				items.add(tempItems.get(i).getName());
			}
		}
		
		return items;
	}
	
	public String[] listAllMarket(){
		String[] toppings = new String[markets.size()];
		int counter = 0;
		if(!markets.isEmpty()){
			for(String key : markets.keySet() ){
				toppings[counter] = key.toString();
				counter++;
			}
		}
		return toppings;
	}
	
	public void chechWish() throws RemoteException{
		if(!wisheList.isEmpty()){
			for( String keyItem : wisheList.keySet()){
				client  = wisheList.get(keyItem);
				String[] itemNameList = keyItem.split(" ");
				String itemName = itemNameList[0].toString();
				
				if(!markets.isEmpty()){
					for(String key : markets.keySet()){
						Market market = markets.get(key);
						for(int i = 0; i < market.listItem().size(); ++i){
							Float price = Float.valueOf(itemNameList[1]);
							if(market.listItem().get(i).getName().toString().equals(itemName) && market.listItem().get(i).getPrice() <= price){
								wisheList.remove(key);
								client.notifyMe(market.getUser().toString(), market.listItem().get(i).getName().toString());
							}
						}
					}
				}
			}
		}
	}


	@Override
	public void wish(String name, String price, CallBack client) throws RemoteException {
		// TODO Auto-generated method stub 
		String item = name + " " + price;
		wisheList.put(item , client);
	}
}
