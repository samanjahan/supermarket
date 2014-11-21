package Bank;
@SuppressWarnings("serial")
final public class RejectedException extends java.rmi.RemoteException {
  public RejectedException() {
    super();
  }
  public RejectedException(String reason) {
    super(reason);
  }
}