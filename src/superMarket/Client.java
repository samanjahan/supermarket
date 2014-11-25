package superMarket;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.StringTokenizer;

@SuppressWarnings("serial")
public class Client extends UnicastRemoteObject implements CallBack{
	protected Client() throws RemoteException {
		
		super();
		
		
		// TODO Auto-generated constructor stub
	}
	
	private static final String DEFAULT_MarketPlace_NAME = "Blocket";
	static Marketplace marketPlace;
	static Market market;
	ItemImpl item;
	private static String MarketPlaceName = "Blocket";
	private static String clientname;
	private static String itemName;
	private static String itemPrice;
	private static String userBuyFrom;
	private static List <String> listWord = new ArrayList<String>();

	static enum CommandName {
		newMarket, deleteMarket, showAllItem, addItem, deleteItem, quit, help, listAllMarket, wish,buy,showMyItem;
	};
	
	private static int getCommand(String userInput){
		if(userInput.equals("newMarket")){
			return 1;
		}
		if(userInput.equals("deleteMarket")){
			itemName = listWord.get(1);
			return 2;
		}
		if(userInput.equals("showAllItem")){
			return 3;
		}
		if(userInput.equals("addItem")){
			itemName = listWord.get(1);
			itemPrice = listWord.get(2);
			return 4;
		}
		if(userInput.equals("deleteItem")){
			return 5;
		}
		if(userInput.equals("quit")){
			return 6;
		}
		if(userInput.equals("help")){
			return 7;
		}
		if(userInput.equals("help")){
			return 7;
		}
		if(userInput.equals("listAllMarket")){
			return 8;
		}if(userInput.equals("wish")){
			itemName = listWord.get(1);
			itemPrice = listWord.get(2);
			return 9;
		}
		if(userInput.equals("buy")){
			itemName = listWord.get(1);
			userBuyFrom = listWord.get(2);
			return 10;
		}
		if(userInput.equals("showMyItem")){
			return 11;
		}
		return 12;
	}

	private static boolean isConnected;

	public static void main(String[] args) throws IOException {
		clientname = null;
		
		
		try {
			try {
				LocateRegistry.getRegistry(1099).list();
			} catch (RemoteException e) {
				LocateRegistry.createRegistry(1099);
			}
			marketPlace = (Marketplace) Naming.lookup(DEFAULT_MarketPlace_NAME);
		} catch (Exception e) {
			System.out.println("The runtime failed: " + e.getMessage());
			System.exit(0);
		}
		
		System.out.println("Connected to SuperMarket: " + MarketPlaceName);
		isConnected = true;
		BufferedReader consoleIn = new BufferedReader(
				new InputStreamReader(System.in));
		StringTokenizer st2;
		
		while (isConnected) {
			String inputClient = consoleIn.readLine();
			if(inputClient !=null){
				st2 = new StringTokenizer(inputClient, " ");
				while(st2.hasMoreElements()){
					String test = (String) st2.nextElement();
					listWord.add(test);
				}
				if(clientname == null){
					clientname = listWord.get(1).toString();
				}
			}
			
			int getCommend  = getCommand(listWord.get(0));
			switch (getCommend){
			case 1:
				if(listWord.size() > 1){
					marketPlace.newMarket(listWord.get(1));
					listWord.clear();
				}else{
					System.out.println("more Value");
				}
				listWord.clear();
				break;
			case 2 :
				marketPlace.deleteMarket(listWord.get(1));
				listWord.clear();
				break;
			case 3:
				for(String s : marketPlace.listAllItem()){
					System.out.println("ItemName: " + s);
				}
				listWord.clear();
				break;
				case 4:
					market = marketPlace.getMarket(clientname.toString());
					market.createItem(itemName, Float.parseFloat(itemPrice));
					listWord.clear();
					marketPlace.chechWish();
					break;
				case 5:
					market.deleteItem(itemName);
					listWord.clear();
					break;
				case 6:
					System.exit(0);
					break;
				case 7:
					for (CommandName commandName : CommandName.values()) {
						System.out.println(commandName);
					}
					listWord.clear();
					break;
				case 8:
					String[] s =  marketPlace.listAllMarket();					
					if(s != null){
						for(int i =0; i < s.length; ++i){
							System.out.println(s[i]);
						}
					}
					listWord.clear();
					break;	
				case 9 :
					Client c = new Client();
					marketPlace.wish(itemName, itemPrice,c);
					listWord.clear();
					break;
				case 10:
					if(marketPlace.buy(itemName, clientname, userBuyFrom)){
						System.out.println("buy has succeeded");
					}else{
						System.out.println("buy has not succeeded");
					}
					listWord.clear();
					break;
				case 11:
					String item = marketPlace.listMyItem(clientname);
					String[] items = item.split(" ");
					if(items[0].length() != 0){
						for(int i = 0 ; i < items.length; ++i){
							System.out.println(items[i]);
						}
					}else{
						System.out.println("You dont have any item");
					}
					listWord.clear();
					break;
				case 12:
					System.out.println("Illegal command");
					listWord.clear();
			}
			
		}
	}

	@Override
	public void notifyMe(String user, String itemName) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("User " +  user + " has " + itemName + " " + "to sell");
		
	}
}
