package com.linketinder.context.builders.match

import com.linketinder.context.builders.interfaces.IBaseDAOBuilder
import com.linketinder.dao.matchdao.MatchDAO
import com.linketinder.dao.matchdao.interfaces.IMatchDAO
import com.linketinder.database.interfaces.IConnection

class MatchDAOBuilder implements IBaseDAOBuilder<IMatchDAO> {

    IConnection connection

    @Override
    MatchDAOBuilder withConnection(IConnection connection) {
        this.connection = connection
        return this
    }

    @Override
    IMatchDAO build() {
        return new MatchDAO(this.connection)
    }

}