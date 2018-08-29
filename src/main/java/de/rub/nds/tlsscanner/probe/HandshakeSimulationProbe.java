/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rub.nds.tlsscanner.probe;

import de.rub.nds.modifiablevariable.util.Modifiable;
import de.rub.nds.tlsattacker.core.config.Config;
import de.rub.nds.tlsattacker.core.constants.CipherSuite;
import de.rub.nds.tlsattacker.core.constants.HandshakeMessageType;
import de.rub.nds.tlsattacker.core.protocol.message.ClientHelloMessage;
import de.rub.nds.tlsattacker.core.protocol.message.extension.ExtensionMessage;
import de.rub.nds.tlsattacker.core.protocol.message.extension.KeyShareExtensionMessage;
import de.rub.nds.tlsattacker.core.state.State;
import de.rub.nds.tlsattacker.core.workflow.DefaultWorkflowExecutor;
import de.rub.nds.tlsattacker.core.workflow.WorkflowExecutor;
import de.rub.nds.tlsattacker.core.workflow.WorkflowTrace;
import de.rub.nds.tlsattacker.core.workflow.WorkflowTraceUtil;
import de.rub.nds.tlsattacker.core.workflow.action.ReceiveAction;
import de.rub.nds.tlsattacker.core.workflow.action.SendAction;
import de.rub.nds.tlsscanner.config.ScannerConfig;
import de.rub.nds.tlsscanner.constants.ProbeType;
import de.rub.nds.tlsscanner.handshakeSimulation.TlsClientConfig;
import de.rub.nds.tlsscanner.handshakeSimulation.TlsClientConfigIO;
import de.rub.nds.tlsscanner.report.SiteReport;
import de.rub.nds.tlsscanner.report.result.HandshakeSimulationResult;
import de.rub.nds.tlsscanner.report.result.ProbeResult;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class HandshakeSimulationProbe extends TlsProbe {
    
    private static final String RESOURCE_FOLDER = "client_configs";
    
    private final List<TlsClientConfig> clientConfigList;
    private final List<CipherSuite> selectedCiphersuiteList;

    public HandshakeSimulationProbe(ScannerConfig config) {
        super(ProbeType.HANDSHAKE_SIMULATION, config, 0);
        this.clientConfigList = new LinkedList<>();
        this.selectedCiphersuiteList = new LinkedList<>();
    }

    @Override
    public ProbeResult executeTest() {
        TlsClientConfigIO clientConfigIO = new TlsClientConfigIO();
        for (File configFile : clientConfigIO.getClientConfigFileList(RESOURCE_FOLDER)) {
            TlsClientConfig clientConfig = clientConfigIO.readConfigFromFile(configFile);
            this.clientConfigList.add(clientConfig);
            Config config = clientConfig.getConfig();
            getScannerConfig().getClientDelegate().applyDelegate(config);
            runClient(clientConfig, config);
        }
        return new HandshakeSimulationResult(clientConfigList, selectedCiphersuiteList);
    }
    
    private void runClient(TlsClientConfig clientConfig, Config config) {
        ClientHelloMessage msgConfig = (ClientHelloMessage) WorkflowTraceUtil.getLastReceivedMessage(HandshakeMessageType.CLIENT_HELLO, clientConfig.getTrace());
        List<ExtensionMessage> extensions = msgConfig.getExtensions();
        for (ExtensionMessage extension : extensions) {
            if (extension instanceof KeyShareExtensionMessage) {
                extension.setExtensionBytes(Modifiable.explicit(extension.getExtensionBytes().getOriginalValue()));
            }
        }
        ClientHelloMessage msg = new ClientHelloMessage(config);
        msg.setProtocolVersion(Modifiable.explicit(msgConfig.getProtocolVersion().getOriginalValue()));
        msg.setExtensions(extensions);
        WorkflowTrace trace = new WorkflowTrace();
        trace.addTlsAction(new SendAction(msg));
        trace.addTlsAction(new ReceiveAction());
        State state = new State(config, trace);
        WorkflowExecutor executor = new DefaultWorkflowExecutor(state);
        executor.executeWorkflow();      
        this.selectedCiphersuiteList.add(state.getTlsContext().getSelectedCipherSuite());
    }

    @Override
    public boolean shouldBeExecuted(SiteReport report) {
        return true;
    }

    @Override
    public void adjustConfig(SiteReport report) {
    }

    @Override
    public ProbeResult getNotExecutedResult() {
        return null;
    }
    
}