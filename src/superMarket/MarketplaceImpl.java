package superMarket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import Bank.Account;
import Bank.Bank;
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

	@Override
	public synchronized boolean buy(String itemName, String userWillBuy ,String userWillSell) throws RemoteException {
		// TODO Auto-generated method stub
		Market marketWill = getMarket(userWillBuy);
		Market marketBuyFrom = getMarket(userWillSell);
		if(marketWill != null && marketBuyFrom != null){
			Float price = marketBuyFrom.getPrice(itemName);
			if(chekBank(userWillBuy, userWillSell,price)){
				marketBuyFrom.deleteItem(itemName);
				marketWill.createItem(itemName, price);
				return true;
			}
		}
		return false;
	}

	@Override
	public String listMyItem(String userName) throws RemoteException {
		// TODO Auto-generated method stub
		Market market = getMarket(userName);
		StringBuilder item =new StringBuilder();
		if(market != null){
			for(int i = 0 ; i <  market.listItem().size(); ++i){
				item.append(market.listItem().get(i).getName() + " ");
			}			
		}
		return item.toString();
	}
	
	public boolean chekBank(String userWillBuy, String userWillSell, Float price) throws RemoteException{
		Bank bank = null;
		Account accountWillSell;
		Account accountWillBuy;
		String DEFAULT_BANK_NAME = "Nordea";
		try {
			try {
				LocateRegistry.getRegistry(1099).list();
			} catch (RemoteException e) {
				LocateRegistry.createRegistry(1099);
			}
			bank = (Bank) Naming.lookup(DEFAULT_BANK_NAME);
		} catch (Exception e) {
			System.out.println("The runtime failed: " + e.getMessage());
			System.exit(0);
		}
		accountWillSell = bank.getAccount(userWillSell);
		accountWillBuy = bank.getAccount(userWillBuy);
		if(accountWillBuy != null && accountWillSell != null){
			if(accountWillBuy.getBalance() >= price){
				accountWillBuy.withdraw(price);
				accountWillSell.deposit(price);
			}else{
				return false;
			}
		}
		return true;
	}
}
