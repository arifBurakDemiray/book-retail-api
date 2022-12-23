package com.bookretail.config.jpa;

import org.hibernate.spatial.dialect.mariadb.MariaDB103SpatialDialect;

import java.sql.Types;

public class MariaDB103SpatialPatchedDialect extends MariaDB103SpatialDialect {
    public MariaDB103SpatialPatchedDialect() {
        super();

        fixCastToDouble();
    }

    private void fixCastToDouble() {
        registerColumnType(Types.DOUBLE, "double");
    }
}
