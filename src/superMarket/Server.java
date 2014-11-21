package superMarket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;



public class Server {
	private static final String USAGE = "java marketrmi.Server <superMarket_rmi_url>";
	private static final String MARKETPLACE = "Blocket";
	public Server(String marketPlaceName) {
		try {
			Marketplace marketImpl = new MarketplaceImpl(marketPlaceName);
			// Register the newly created object at rmiregistry.
			try {
				LocateRegistry.getRegistry(1099).list();
			} catch (RemoteException e) {
				LocateRegistry.createRegistry(1099);
			}
			Naming.rebind(marketPlaceName, marketImpl);
			System.out.println(marketImpl + " is ready.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		if (args.length > 1
				|| (args.length > 0 && args[0].equalsIgnoreCase("-h"))) {
			System.out.println(USAGE);
			System.exit(1);
		}
		String bankName;
		if (args.length > 0) {
			bankName = args[0];
		} else {
			bankName = MARKETPLACE;
		}
		new Server(bankName);
	}
}
