package org.flowvisor.api.handlers.configuration;

import java.util.List;
import java.util.Map;

import org.flowvisor.api.APIAuth;
import org.flowvisor.api.APIUserCred;
import org.flowvisor.api.handlers.ApiHandler;
import org.flowvisor.api.handlers.HandlerUtils;
import org.flowvisor.classifier.FVClassifier;
import org.flowvisor.config.ConfigError;
import org.flowvisor.config.FVConfig;
import org.flowvisor.config.FlowSpaceImpl;
import org.flowvisor.config.InvalidDropPolicy;
import org.flowvisor.config.SliceImpl;
import org.flowvisor.config.SwitchImpl;
import org.flowvisor.exceptions.DuplicateControllerException;
import org.flowvisor.exceptions.MissingRequiredField;
import org.flowvisor.exceptions.PermissionDeniedException;
import org.flowvisor.message.lldp.LLDPUtil;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParamsType;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

public class UpdateLLDPHeader implements ApiHandler<Map<String, Object>> {
	
	@Override
	public JSONRPC2Response process(Map<String, Object> params) {
		JSONRPC2Response resp = null;
		
		try {
			short etherType = HandlerUtils.<Number>fetchField("ethertype", params, true, null).shortValue();
			String dstMACAddr = HandlerUtils.<String>fetchField("dstMACAddr", params, false, "01:80:c2:00:00:0e");
			updateLLDPHeader(etherType, dstMACAddr);
			
			resp = new JSONRPC2Response(true, 0);
		} catch (ClassCastException e) {
			resp = new JSONRPC2Response(new JSONRPC2Error(JSONRPC2Error.INVALID_PARAMS.getCode(), 
					cmdName() + ": " + e.getMessage()), 0);
		} catch (MissingRequiredField e) {
			resp = new JSONRPC2Response(new JSONRPC2Error(JSONRPC2Error.INVALID_PARAMS.getCode(), 
					cmdName() + ": " + e.getMessage()), 0);
		} 
		return resp;
	}

	/*
	 */
	private void updateLLDPHeader(short etherType, String dstMACAddr) {
	    
        LLDPUtil.updateLLDPEtherType(etherType);
        if (dstMACAddr != null) {
            LLDPUtil.updateLLDPDstMACAddr(dstMACAddr);
        }
    }

    @Override
	public JSONRPC2ParamsType getType() {
		return JSONRPC2ParamsType.OBJECT;
	}

	@Override
	public String cmdName() {
		return "update-lldp-header";
	}
}
