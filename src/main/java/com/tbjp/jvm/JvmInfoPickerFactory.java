package com.tbjp.jvm;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午3:57
 * To change this template use File | Settings | File Templates.
 */
public class JvmInfoPickerFactory {
    public static final String PICKER_TYPE = "Local";

    public static JvmInfoPicker create(String type) {
        return LocalJvmInfoPicker.getInstance();
    }
}
