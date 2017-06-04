package com.banks;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import javax.persistence.Column;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * Created by banks on 6/4/17.
 */
public class EncryptionInterceptor extends EmptyInterceptor {

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return doSaveOrFlushAction(entity, state);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        return doSaveOrFlushAction(entity, currentState);
    }

    /**
     * This method is invoked after any transaction is committed.
     * After a typical transaction, the db entities are propagated back to the objects used to save them.
     * This means that when we save, our object will have encrypted properties. This method will be used to decrypt them.
     *
     * @param entities entities involved in committed transaction
     */
    @Override
    public void postFlush(Iterator entities) {
        entities.forEachRemaining(entity -> {
            Field[] declaredFields = entity.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Class<?> type = declaredField.getType();
                if (!type.equals(String.class)) {
                    continue;
                }

                boolean isColumn = declaredField.isAnnotationPresent(Column.class);
                if (!isColumn) {
                    continue;
                }

                try {
                    PropertyDescriptor pd = new PropertyDescriptor(declaredField.getName(), entity.getClass());
                    String value = (String) pd.getReadMethod().invoke(entity);
                    if (value != null && !value.trim().isEmpty()) {
                        pd.getWriteMethod().invoke(entity, SimpleAES.decrypt(value));
                    }
                } catch (Exception e) {
                    //log error here
                }

            }
        });
    }

    private boolean doSaveOrFlushAction(Object entity, Object[] state) {
        try {
            for (int i = 0; i < state.length; i++) {
                if (state[i] instanceof String) {
                    String plainText = (String) state[i];
                    if (plainText != null && !plainText.trim().isEmpty()) {
                        state[i] = SimpleAES.encrypt(plainText);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            //log error
        }
        return false;
    }

}
