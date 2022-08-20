package com.bobocode.petros.bibernate.session.factory;

import com.bobocode.petros.bibernate.session.Session;

public interface SessionFactory {

    Session openSession();

}
