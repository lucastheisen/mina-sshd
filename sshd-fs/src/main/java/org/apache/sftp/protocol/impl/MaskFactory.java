package org.apache.sftp.protocol.impl;


import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


public class MaskFactory {
    public static <E extends Enum<E> & Maskable<E>> EnumSet<E> fromMask( int mask, Class<E> type ) {
        List<E> maskList = new ArrayList<>();
        for ( E maskValue : type.getEnumConstants() ) {
            if ( (mask & maskValue.getValue()) == maskValue.getValue() ) {
                maskList.add( maskValue );
            }
        }
        return maskList.isEmpty() ? EnumSet.noneOf( type ) : EnumSet.copyOf( maskList );
    }

    public static <E extends Enum<E> & Maskable<E>> int toMask( EnumSet<E> enumSet ) {
        int mask = 0;
        if ( enumSet != null && !enumSet.isEmpty() ) {
            for ( E maskValue : enumSet ) {
                mask |= maskValue.getValue();
            }
        }
        return mask;
    }
}