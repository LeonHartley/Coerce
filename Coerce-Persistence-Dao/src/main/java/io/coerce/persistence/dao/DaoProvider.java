package io.coerce.persistence.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DaoProvider {
    private final Logger log = LogManager.getLogger(DaoProvider.class);

    private final Map<Class<? extends Dao>, Dao> daoInstanceCache;
    private final InvocationHandler invocationHandler;

    public DaoProvider() {
        this.daoInstanceCache = new ConcurrentHashMap<>();
        this.invocationHandler = new DaoProxyInvocationHandler();
    }

    public <T extends Dao> T getDao(Class<T> clazz) {
        if (this.daoInstanceCache.containsKey(clazz)) {
            final Dao daoInstance = this.daoInstanceCache.get(clazz);

            return (T) this.daoInstanceCache.get(clazz);
        }

        return this.createDaoProxy(clazz);
    }

    private <T extends Dao> T createDaoProxy(Class<T> clazz) {
        final Dao instance = (Dao) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                this.invocationHandler);

        this.daoInstanceCache.put(clazz, instance);

        return (T) instance;
    }
}
