package edu.wust.durui.model.builder.machine;

import edu.wust.durui.model.Machine;
import edu.wust.durui.model.cornerstone.Clock;
import edu.wust.durui.model.layers.t0.T0;
import edu.wust.durui.model.layers.t1.JSJF;
import edu.wust.durui.model.layers.t2.PMFQB;
import edu.wust.durui.model.layers.t3.T3;

public class SJFMQFBBuilder implements MachineBuilder {
    public Machine build() {
        return new Machine()
                .setTAG("SJFMQFB2")
                .setClock(new Clock("00:00"))
                .setSubmitImitator(new T0())
                .setJobScheduler(new JSJF())
                .setProcessScheduler(new PMFQB().setLIMIT(2))
                .setLandfilImitator(new T3());
    }
}
