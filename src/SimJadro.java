public class SimJadro {
    private int numberOfReplications;
    private volatile boolean stopRequested = false;

    public SimJadro(int numberOfReplications) {
        this.numberOfReplications = numberOfReplications;
    }

    public void simuluj() {
        beforeReps();
        for (int i = 0; i < numberOfReplications && !stopRequested; i++) {
            beforeRep();
            doRep();
            afterRep();
        }
        afterReps();
    }

    void doRep() {
    }

    void beforeReps() {

    }

    void afterReps() {

    }

    void beforeRep() {

    }

    void afterRep() {

    }

    public int getNumberOfReplications() {
        return numberOfReplications;
    }

    public void stopSimulaciu() {
        stopRequested = true;
    }
}
