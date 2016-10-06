package io.coerce.persistence.dao;

import com.google.common.collect.Maps;
import io.coerce.persistence.dao.annotations.Transaction;

import java.awt.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DaoProxyInvocationHandler implements InvocationHandler {

    private final Map<String, List<String>> cachedParameters = Maps.newHashMap();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.isAnnotationPresent(Transaction.class)) {
            final String transactionIdentifier = method.getDeclaringClass().getSimpleName() + "." + method.getName();
            final Transaction transactionInfo = method.getDeclaredAnnotation(Transaction.class);

            final String query = transactionInfo.query();

            if(args.length == 0) {
                final List<String> parameters = this.cachedParameters.containsKey(transactionIdentifier) ?
                        this.cachedParameters.get(transactionIdentifier) :
                        new ArrayList<>();

                for(Parameter parameter : method.getParameters()) {
                    parameters.add(parameter.getName());
                }
            }

            System.out.println("attempting to execute query " + query + " for dao: " + );
        }

        return null;
    }
}
