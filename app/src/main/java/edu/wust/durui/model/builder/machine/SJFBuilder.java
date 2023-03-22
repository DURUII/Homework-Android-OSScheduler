package edu.wust.durui.model.builder.machine;

import edu.wust.durui.model.Machine;
import edu.wust.durui.model.cornerstone.Clock;
import edu.wust.durui.model.layers.t0.T0;
import edu.wust.durui.model.layers.t1.JSJF;
import edu.wust.durui.model.layers.t2.PFCFS;
import edu.wust.durui.model.layers.t3.T3;

public class SJFBuilder implements MachineBuilder {

    public Machine build() {
        return new Machine()
                .setTAG("SJF")
                .setClock(new Clock("00:00"))
                .setSubmitImitator(new T0())
                .setJobScheduler(new JSJF())
                .setProcessScheduler(new PFCFS().setLIMIT(1))
                .setLandfilImitator(new T3());
    }
}