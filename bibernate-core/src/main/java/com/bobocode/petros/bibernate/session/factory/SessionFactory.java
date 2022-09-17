package com.bobocode.petros.bibernate.session.factory;

import com.bobocode.petros.bibernate.session.Session;

/**
 * Primary interface which implementation is responsible for creating instances of {@link Session}.
 */
public interface SessionFactory {

    /**
     * Creates an instance of {@link Session}
     *
     * @return instance of {@link Session}
     */
    Session openSession();

}
