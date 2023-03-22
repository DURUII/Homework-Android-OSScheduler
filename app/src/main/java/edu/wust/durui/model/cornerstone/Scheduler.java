package edu.wust.durui.model.cornerstone;

public abstract class Scheduler implements Observer {
    protected int LIMIT = Integer.MAX_VALUE;
    protected Scheduler superior = null;
    protected Scheduler inferior = null;

    public Scheduler setLIMIT(int LIMIT) {
        this.LIMIT = LIMIT;
        return this;
    }

    public void setSuperior(Scheduler superior) {
        this.superior = superior;
    }

    public void setInferior(Scheduler inferior) {
        this.inferior = inferior;
    }

    @Override
    public void observe(Observable target) {
        target.attachObserver(this);
    }

    @Override
    public void forsake(Observable target) {
        target.detachObserver(this);
    }

    public abstract boolean add(Task t);

    public abstract boolean reportToSuperior();

    public abstract boolean notifyToInferior();

    public abstract boolean rebase();

    public abstract int size();

    public abstract boolean isEmpty();

    public abstract void shuffle();
}
