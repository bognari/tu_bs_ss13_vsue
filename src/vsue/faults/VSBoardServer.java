package vsue.faults;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;

/**
 * @author Stephan
 * 
 */
public class VSBoardServer {
  /**
	 * 
	 */
  static final String          NAME  = "VSBoard";

  private static final boolean debug = false;

  /**
   * @param args
   */
  public static void main(final String[] args) {
    VSRemoteObjectManager.setRPCSymmantic(VSRPCsemantics.AMO);
    VSBoardServer server = null;

    try {
      server = new VSBoardServer();
    } catch (final RemoteException re) {
      System.err.println("Server could not be started.");
      re.printStackTrace();
      System.exit(0);
    }

    try {
      server.start();
      System.out.println("server is running");
    } catch (RemoteException | AlreadyBoundException e) {
      System.err.println("Server could not be started");
      e.printStackTrace();
    }
  }

  /**
   * @throws RemoteException
   */
  public VSBoardServer() throws RemoteException {
  }

  /**
   * startet den VSBoardServer, erstellt ein VSBoard und exportiert dieses
   * 
   * @throws RemoteException
   * @throws AlreadyBoundException
   * @throws ExportException
   */
  public void start() throws RemoteException, AlreadyBoundException, ExportException {
    final VSBoard board = new VSBoardImpl();
    if (VSBoardServer.debug) {
      System.out.println("board created");
    }

    final VSBoard remBoard = (VSBoard) VSRemoteObjectManager.getInstance().exportObject(board);
    if (VSBoardServer.debug) {
      System.out.println("board exported");
    }

    LocateRegistry.createRegistry(Registry.REGISTRY_PORT).rebind(VSBoardServer.NAME, remBoard);
    if (VSBoardServer.debug) {
      System.out.println("board in registry");
    }
  }
}
