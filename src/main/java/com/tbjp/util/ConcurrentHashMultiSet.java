package com.tbjp.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午3:16
 * To change this template use File | Settings | File Templates.
 */
public class ConcurrentHashMultiSet<E> {
    private final transient ConcurrentMap<E, AtomicInteger> counterMap;

    public ConcurrentHashMultiSet() {
        this.counterMap = new ConcurrentHashMap();
    }

    public int add(E element) {
        if (element == null) {
            return 0;
        }

        AtomicInteger existingCounter = (AtomicInteger) this.counterMap.get(element);
        if (existingCounter == null) {
            AtomicInteger newCounter = new AtomicInteger();
            existingCounter = (AtomicInteger) this.counterMap.putIfAbsent(element, newCounter);
            if (existingCounter == null) {
                existingCounter = newCounter;
            }

        }

        return existingCounter.incrementAndGet();
    }

    public Set<E> elementSet() {
        return this.counterMap.keySet();
    }

    public int count(E element) {
        if (element == null) {
            return 0;
        }
        AtomicInteger existingCounter = (AtomicInteger) this.counterMap.get(element);
        return existingCounter == null ? 0 : existingCounter.get();
    }
}
