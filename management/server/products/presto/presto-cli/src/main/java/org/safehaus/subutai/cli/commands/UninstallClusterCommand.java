package org.safehaus.subutai.cli.commands;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.safehaus.subutai.api.presto.Config;
import org.safehaus.subutai.api.presto.Presto;
import org.safehaus.subutai.core.tracker.api.Tracker;
import org.safehaus.subutai.common.tracker.ProductOperationState;
import org.safehaus.subutai.common.tracker.ProductOperationView;

import java.util.UUID;


/**
 * Displays the last log entries
 */
@Command (scope = "presto", name = "uninstall-cluster", description = "Command to uninstall Presto cluster")
public class UninstallClusterCommand extends OsgiCommandSupport {

	@Argument (index = 0, name = "clusterName", description = "The name of the cluster.", required = true, multiValued = false)
	String clusterName = null;
	private Presto prestoManager;
	private Tracker tracker;

	public Tracker getTracker() {
		return tracker;
	}

	public void setTracker(Tracker tracker) {
		this.tracker = tracker;
	}

	public Presto getPrestoManager() {
		return prestoManager;
	}

	public void setPrestoManager(Presto prestoManager) {
		this.prestoManager = prestoManager;
	}

	protected Object doExecute() {
		UUID uuid = prestoManager.uninstallCluster(clusterName);
		int logSize = 0;
		while (!Thread.interrupted()) {
			ProductOperationView po = tracker.getProductOperation(Config.PRODUCT_KEY, uuid);
			if (po != null) {
				if (logSize != po.getLog().length()) {
					System.out.print(po.getLog().substring(logSize, po.getLog().length()));
					System.out.flush();
					logSize = po.getLog().length();
				}
				if (po.getState() != ProductOperationState.RUNNING) {
					break;
				}
			} else {
				System.out.println("Product operation not found. Check logs");
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				break;
			}
		}
		return null;
	}
}
