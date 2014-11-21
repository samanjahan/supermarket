package superMarket;

import java.rmi.RemoteException;


public class Test {

	/**
	 * @param args
	 * @throws RemoteException 
	 */
	public static void main(String[] args) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("hej");
	/*	MarketImpl mk = new MarketImpl("saman");
		mk.createItem("Camera", 300);
		mk.createItem("Video", 100);
		
		for(int i = 0 ; i < mk.listItem().size(); ++i){
			System.out.println("first : " + " " + mk.listItem().size() + " " + mk.listItem().get(i).getName() + " " +  mk.listItem().get(i).getPrice());
		}
		mk.deleteItem("Video");
		for(int i = 0 ; i < mk.listItem().size(); ++i){
			System.out.println("next : " +" " + mk.listItem().size() + " " + mk.listItem().get(i).getName() + " " +  mk.listItem().get(i).getPrice());
		}*/
		
		MarketplaceImpl mkp = new MarketplaceImpl("Blocket");
		MarketImpl marketImpl;
		
		marketImpl = (MarketImpl) mkp.newMarket("Saman");
		marketImpl = (MarketImpl) mkp.newMarket("Ashkan");
		
		Market m = mkp.getMarket("Saman");
		System.out.println("Market " + m);
		m.createItem("Video",100);
		m.createItem("Carmera",200);
		
		Market m1 = mkp.getMarket("Ashkan");
		m1.createItem("Tv",120);
		m1.createItem("Radio",260);
		
		for(String marketHolder : mkp.listMarket()){
			System.out.println(marketHolder);
		}
		
	}

}
