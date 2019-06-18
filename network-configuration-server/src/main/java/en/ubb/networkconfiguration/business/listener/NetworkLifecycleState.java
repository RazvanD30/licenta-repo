package en.ubb.networkconfiguration.business.listener;

public enum NetworkLifecycleState {
        NEW(false),
        INITIALIZING(false),
        INITIALIZED(false),
        STARTING_PREP(false),
        STARTING(true),
        STARTED(true),
        ITERATION_GROUP_STARTED(true),
        ITERATION_GROUP_STOPPED(true),
        SCORE_IMPROVED(true),
        STOPPING_PREP(true),
        STOPPING(false),
        STOPPED(false),
        DESTROYING(false),
        DESTROYED(false),
        FAILED(false);

        private final boolean available;

        private NetworkLifecycleState(boolean available) {
            this.available = available;
        }

        public boolean isAvailable() {
            return available;
        }

}
