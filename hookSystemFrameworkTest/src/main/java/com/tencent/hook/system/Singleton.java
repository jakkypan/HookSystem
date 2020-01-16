package com.tencent.hook.system;

public abstract class Singleton<T, P> {
    private volatile T mInstance;

    /**
     * Override this to return singleton instance.
     *
     * @param p Parameter.
     * @return Singleton instance.
     */
    protected abstract T create(P p);

    /**
     * Returns the singleton instance.
     *
     * @param p Parameter.
     * @return Singleton instance.
     */
    public final T get(P p) {
        if (mInstance == null) {
            synchronized (this) {
                if (mInstance == null) {
                    mInstance = create(p);
                }
            }
        }
        return mInstance;
    }
}
