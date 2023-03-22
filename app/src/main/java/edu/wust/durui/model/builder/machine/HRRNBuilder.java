package edu.wust.durui.model.builder.machine;

import edu.wust.durui.model.Machine;
import edu.wust.durui.model.cornerstone.Clock;
import edu.wust.durui.model.layers.t0.T0;
import edu.wust.durui.model.layers.t1.JHRRN;
import edu.wust.durui.model.layers.t2.PFCFS;
import edu.wust.durui.model.layers.t3.T3;

public class HRRNBuilder implements MachineBuilder {

    public Machine build() {
        return new Machine()
                .setTAG("HRRN")
                .setClock(new Clock("00:00"))
                .setSubmitImitator(new T0())
                .setJobScheduler(new JHRRN())
                .setProcessScheduler(new PFCFS().setLIMIT(1))
                .setLandfilImitator(new T3());
    }
}