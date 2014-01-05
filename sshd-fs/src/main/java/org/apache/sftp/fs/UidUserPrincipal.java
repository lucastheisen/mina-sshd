package org.apache.sftp.fs;

import java.nio.file.attribute.UserPrincipal;

public class UidUserPrincipal implements UserPrincipal {
    private int uid;
    
    public UidUserPrincipal( int uid ) {
        this.uid = uid;
    }
    
    @Override
    public String getName() {
        // its unsigned in sftp so...
        return Long.toString( uid );
    }

}
