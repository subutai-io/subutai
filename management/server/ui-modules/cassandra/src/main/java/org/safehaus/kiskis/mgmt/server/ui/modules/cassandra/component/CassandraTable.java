package org.safehaus.kiskis.mgmt.server.ui.modules.cassandra.component;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import static java.lang.Compiler.command;
import org.safehaus.kiskis.mgmt.server.ui.modules.cassandra.CassandraModule;
import org.safehaus.kiskis.mgmt.shared.protocol.CassandraClusterInfo;
import org.safehaus.kiskis.mgmt.shared.protocol.api.CommandManagerInterface;

import java.util.List;
import java.util.UUID;
import org.safehaus.kiskis.mgmt.shared.protocol.Task;
import org.safehaus.kiskis.mgmt.shared.protocol.enums.TaskStatus;
import sun.management.resources.agent;

/**
 * Created with IntelliJ IDEA.
 * User: daralbaev
 * Date: 11/30/13
 * Time: 6:56 PM
 */
public class CassandraTable extends Table {
    private final CommandManagerInterface commandManager;
    private IndexedContainer container;
    private final CassandraModule.ModuleComponent parent;

    public CassandraTable(CommandManagerInterface commandManager, final CassandraModule.ModuleComponent window) {
        this.commandManager = commandManager;
        this.parent = window;

        this.setCaption("Cassandra clusters");
        this.setContainerDataSource(getCassandraContainer());

        this.setWidth("100%");
        this.setHeight(100, Sizeable.UNITS_PERCENTAGE);

        this.setPageLength(20);
        this.setSelectable(true);
        this.setImmediate(true);
        this.addListener(new Table.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                String caption = container.getItem(event.getProperty().getValue())
                        .getItemProperty(CassandraClusterInfo.NAME_LABEL).getValue().toString();
                List<UUID> list = (List<UUID>) container.getItem(event.getProperty().getValue())
                        .getItemProperty(CassandraClusterInfo.NODES_LABEL).getValue();

                Window window = new NodesWindow(caption, list);
                window.setModal(true);
                getApplication().getMainWindow().addWindow(window);
            }
        });
    }

    private IndexedContainer getCassandraContainer() {
        container = new IndexedContainer();

        // Create the container properties
        container.addContainerProperty(CassandraClusterInfo.UUID_LABEL, UUID.class, "");
        container.addContainerProperty(CassandraClusterInfo.NAME_LABEL, String.class, "");
        container.addContainerProperty(CassandraClusterInfo.DATADIR_LABEL, String.class, "");
        container.addContainerProperty(CassandraClusterInfo.COMMITLOGDIR_LABEL, String.class, "");
        container.addContainerProperty(CassandraClusterInfo.SAVEDCACHEDIR_LOG, String.class, "");
//        Button startButton  = new Button("Start");
//        startButton.addListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
////                Task task = new Task();
////                task.setDescription("Starting cluster");
////                task.setTaskStatus(TaskStatus.NEW);
//                getWindow().showNotification("Start cassandra");
////               Command command = buildCommand(agent.getUuid(), dataDirCommand, reqSeqNumber, taskUuid, args);
////                    cassandraWizard.runCommand(command);
//            }
//        });
//        Button stopButton  = new Button("Start");
//        stopButton.addListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
////                Task task = new Task();
////                task.setDescription("Starting cluster");
////                task.setTaskStatus(TaskStatus.NEW);
//                getWindow().showNotification("Stop cassandra");
////               Command command = buildCommand(agent.getUuid(), dataDirCommand, reqSeqNumber, taskUuid, args);
////                    cassandraWizard.runCommand(command);
//            }
//        });
        
        container.addContainerProperty("Start", Button.class, "");
        container.addContainerProperty("Stop", Button.class, "");
        container.addContainerProperty("Destroy", Button.class, "");
        container.addContainerProperty(CassandraClusterInfo.NODES_LABEL, List.class, 0);
        container.addContainerProperty(CassandraClusterInfo.SEEDS_LABEL, List.class, 0);

        // Create some orders
        List<CassandraClusterInfo> cdList = commandManager.getCassandraClusterData();
        for (CassandraClusterInfo cluster : cdList) {
            addOrderToContainer(container, cluster);
        }

        return container;
    }

    private void addOrderToContainer(Container container, CassandraClusterInfo cd) {
        Object itemId = container.addItem();
        Item item = container.getItem(itemId);
        item.getItemProperty(CassandraClusterInfo.UUID_LABEL).setValue(cd.getUuid());
        item.getItemProperty(CassandraClusterInfo.NAME_LABEL).setValue(cd.getName());
        item.getItemProperty(CassandraClusterInfo.DATADIR_LABEL).setValue(cd.getDataDir());
        item.getItemProperty(CassandraClusterInfo.COMMITLOGDIR_LABEL).setValue(cd.getCommitLogDir());
        item.getItemProperty(CassandraClusterInfo.SAVEDCACHEDIR_LOG).setValue(cd.getSavedCacheDir());
         Button startButton  = new Button("Start");
        startButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
//                Task task = new Task();
//                task.setDescription("Starting cluster");
//                task.setTaskStatus(TaskStatus.NEW);
                getWindow().showNotification("Start cassandra cluster");
//               Command command = buildCommand(agent.getUuid(), dataDirCommand, reqSeqNumber, taskUuid, args);
//                    cassandraWizard.runCommand(command);
            }
        });
        Button stopButton  = new Button("Start");
        stopButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
//                Task task = new Task();
//                task.setDescription("Starting cluster");
//                task.setTaskStatus(TaskStatus.NEW);
                getWindow().showNotification("Stop cassandra cluster");
//               Command command = buildCommand(agent.getUuid(), dataDirCommand, reqSeqNumber, taskUuid, args);
//                    cassandraWizard.runCommand(command);
            }
        });
        Button destroyButton  = new Button("Destroy");
        stopButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
//                Task task = new Task();
//                task.setDescription("Starting cluster");
//                task.setTaskStatus(TaskStatus.NEW);
                getWindow().showNotification("Destroy cassandra cluster");
//               Command command = buildCommand(agent.getUuid(), dataDirCommand, reqSeqNumber, taskUuid, args);
//                    cassandraWizard.runCommand(command);
            }
        });
        item.getItemProperty("Start").setValue(startButton);
        item.getItemProperty("Stop").setValue(stopButton);
        item.getItemProperty("Destroy").setValue(destroyButton);
        item.getItemProperty(CassandraClusterInfo.NODES_LABEL).setValue(cd.getNodes());
        item.getItemProperty(CassandraClusterInfo.SEEDS_LABEL).setValue(cd.getSeeds());
    }

    public void refreshDatasource() {
        this.setContainerDataSource(getCassandraContainer());
    }
}
