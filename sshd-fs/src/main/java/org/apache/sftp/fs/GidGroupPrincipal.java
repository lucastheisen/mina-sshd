package org.apache.sftp.fs;


import java.nio.file.attribute.GroupPrincipal;


public class GidGroupPrincipal implements GroupPrincipal {
    private int gid;

    public GidGroupPrincipal( int gid ) {
        this.gid = gid;
    }

    @Override
    public String getName() {
        // its unsigned in sftp so...
        return Long.toString( gid );
    }

}
