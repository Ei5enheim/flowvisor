package org.flowvisor.api.handlers.configuration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.flowvisor.api.handlers.ApiHandler;
import org.flowvisor.config.ConfigError;
import org.flowvisor.config.SliceImpl;
import org.flowvisor.message.lldp.LLDPUtil;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParamsType;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

public class GetLLDPHeaderInfo implements ApiHandler<Object> {

	@SuppressWarnings("unchecked")
	@Override
	public JSONRPC2Response process(Object params) {
		JSONRPC2Response resp = null;
		try {
			HashMap<String, Object> data = new HashMap<String, Object>();
            String etherType = LLDPUtil.getLLDPEtherType();
            String dstMACAddr = LLDPUtil.getDstMACAddr();
            data.put("ethertype", etherType);
            data.put("dstMACAddr", dstMACAddr);
			resp = new JSONRPC2Response(data, 0);
		} catch (Exception e) {
			resp = new JSONRPC2Response(new JSONRPC2Error(JSONRPC2Error.INTERNAL_ERROR.getCode(), 
					cmdName() + ": Unable to fetch slice list : " + e.getMessage()), 0);
		} 
		return resp;
	}

	@Override
	public JSONRPC2ParamsType getType() {
		return JSONRPC2ParamsType.NO_PARAMS;
	}

	@Override
	public String cmdName() {
		return "get-lldp-header-info";
	}

}
