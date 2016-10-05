package io.coerce.persistence.dao;

import io.coerce.persistence.dao.annotations.Transaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DaoProxyInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.isAnnotationPresent(Transaction.class)) {
            final Transaction transactionInfo = method.getDeclaredAnnotation(Transaction.class);

            final String query = transactionInfo.query();

            System.out.println("attempting to execute query " + query + " for dao: " + method.getDeclaringClass().getSimpleName() + "." + method.getName());
        }

        return null;
    }
}
