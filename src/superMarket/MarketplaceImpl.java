package superMarket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import Bank.RejectedException;

@SuppressWarnings("serial")
public class MarketplaceImpl extends UnicastRemoteObject implements Marketplace {
	
	private String marketPlaceName;
	private Map<String, Market> markets = new HashMap<String, Market>();

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
}
