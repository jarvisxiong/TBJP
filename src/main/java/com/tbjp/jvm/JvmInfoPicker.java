package com.tbjp.jvm;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午3:56
 * To change this template use File | Settings | File Templates.
 */
public abstract interface JvmInfoPicker {
    public abstract String pickJvmEnvironmentInfo();

    public abstract String pickJvmRumtimeInfo();

    public abstract String getJvmInstanceCode();
}