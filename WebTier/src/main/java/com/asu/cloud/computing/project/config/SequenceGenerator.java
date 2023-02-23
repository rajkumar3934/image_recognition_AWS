package com.asu.cloud.computing.project.config;

import java.util.concurrent.atomic.AtomicInteger;

public final class SequenceGenerator {
	private static final AtomicInteger sequence = new AtomicInteger(1);

    private SequenceGenerator() {}

    public static int generate(){
        return sequence.getAndIncrement();
    }
}
