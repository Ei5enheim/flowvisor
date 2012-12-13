package org.flowvisor.message.statistics;

import java.util.Iterator;
import org.flowvisor.classifier.FVClassifier;
import org.flowvisor.log.FVLog;
import org.flowvisor.log.LogLevel;
import org.flowvisor.message.FVMessageUtil;
import org.flowvisor.message.FVStatisticsReply;
import org.flowvisor.message.FVStatisticsRequest;
import org.flowvisor.slicer.FVSlicer;
import org.openflow.protocol.statistics.OFPortStatisticsReply;
import org.openflow.protocol.statistics.OFStatistics;

public class FVPortStatisticsReply extends OFPortStatisticsReply implements
		SlicableStatistic, ClassifiableStatistic {



	@Override
	public void classifyFromSwitch(FVStatisticsReply msg,
			FVClassifier fvClassifier) {
		FVSlicer fvSlicer = FVMessageUtil.untranslateXid(msg, fvClassifier);
        if (fvSlicer == null) {
                FVLog.log(LogLevel.WARN, fvClassifier,
                                "dropping unclassifiable port stats reply: " + this);
                return;
        }
        for (Iterator<OFStatistics> it = msg.getStatistics().iterator(); it
                        .hasNext();) {
                OFStatistics stat = it.next();
                if (stat instanceof OFPortStatisticsReply) {
                        OFPortStatisticsReply portStat = (OFPortStatisticsReply) stat;
                        if (!fvSlicer.portInSlice(portStat.getPortNumber())) {
                        		FVLog.log(LogLevel.DEBUG, fvClassifier, "Dropping ", portStat, 
                        				" because port not in slice.");
                                it.remove();
                                msg.setLengthU(msg.getLengthU() - portStat.computeLength());
                        }
                }
        }
       
        fvSlicer.sendMsg(msg, fvClassifier);
		
	}

	@Override
	public void sliceFromController(FVStatisticsRequest msg,
			FVClassifier fvClassifier, FVSlicer fvSlicer) {
		FVLog.log(LogLevel.WARN, fvSlicer, "dropping unexpected msg: " + this);
		
	}
}
