package com.wxxr.trading.core.api;
import java.util.HashMap;
import java.util.Map;

import com.wxxr.common.util.Assert;

public class Context {
	private static ThreadLocal<Context> threadLocal = new ThreadLocal<Context>();
	
    /** The client creates an instance of this class for each key.
     */
    public static class Key<T> {
    }

    /**
     * The client can register a factory for lazy creation of the
     * instance.
     */
    public static interface Factory<T> {
        T make(Context c);
    };

    /**
     * The underlying map storing the data.
     * We maintain the invariant that this table contains only
     * mappings of the form
     * Key<T> -> T or Key<T> -> Factory<T> */
    private Map<Key<?>,Object> ht = new HashMap<Key<?>,Object>();

    /** Set the factory for the key in this context. */
    public <T> void put(Key<T> key, Factory<T> fac) {
        checkState(ht);
        Object old = ht.put(key, fac);
        if (old != null)
            throw new AssertionError("duplicate context value");
        checkState(ft);
        ft.put(key, fac); // cannot be duplicate if unique in ht
    }

    /** Set the value for the key in this context. */
    public <T> void put(Key<T> key, T data) {
        if (data instanceof Factory<?>)
            throw new AssertionError("T extends Context.Factory");
        checkState(ht);
        Object old = ht.put(key, data);
        if (old != null && !(old instanceof Factory<?>) && old != data && data != null)
            throw new AssertionError("duplicate context value");
    }

    /** Get the value for the key in this context. */
    public <T> T get(Key<T> key) {
        checkState(ht);
        Object o = ht.get(key);
        if (o instanceof Factory<?>) {
            Factory<?> fac = (Factory<?>)o;
            o = fac.make(this);
            if (o instanceof Factory<?>)
                throw new AssertionError("T extends Context.Factory");
            Assert.isTrue(ht.get(key) == o);
        }

        /* The following cast can't fail unless there was
         * cheating elsewhere, because of the invariant on ht.
         * Since we found a key of type Key<T>, the value must
         * be of type T.
         */
        return Context.<T>uncheckedCast(o);
    }

    private Context() {}

    /**
     * The table of preregistered factories.
     */
    private Map<Key<?>,Factory<?>> ft = new HashMap<Key<?>,Factory<?>>();

    /*
     * The key table, providing a unique Key<T> for each Class<T>.
     */
    private Map<Class<?>, Key<?>> kt = new HashMap<Class<?>, Key<?>>();

    private <T> Key<T> key(Class<T> clss) {
        checkState(kt);
        Key<T> k = uncheckedCast(kt.get(clss));
        if (k == null) {
            k = new Key<T>();
            kt.put(clss, k);
        }
        return k;
    }

    public <T> T get(Class<T> clazz) {
        return get(key(clazz));
    }

    public <T> void put(Class<T> clazz, T data) {
        put(key(clazz), data);
    }
    public <T> void put(Class<T> clazz, Factory<T> fac) {
        put(key(clazz), fac);
    }

    /**
     * TODO: This method should be removed and Context should be made type safe.
     * This can be accomplished by using class literals as type tokens.
     */
    @SuppressWarnings("unchecked")
    private static <T> T uncheckedCast(Object o) {
        return (T)o;
    }

    public void dump() {
        for (Object value : ht.values())
            System.err.println(value == null ? null : value.getClass());
    }

    public void clear() {
        ht = null;
        kt = null;
        ft = null;
    }

    private static void checkState(Map<?,?> t) {
        if (t == null)
            throw new IllegalStateException();
    }
}
