package com.linketinder.context.builders.candidate

import com.linketinder.context.builders.interfaces.IDAOBuilder
import com.linketinder.dao.candidatedao.LanguageDAO
import com.linketinder.dao.candidatedao.interfaces.ILanguageDAO
import com.linketinder.database.interfaces.*

class LanguageDAOBuilder implements IDAOBuilder<ILanguageDAO> {

    IConnection connection
    IDBService dbService

    @Override
    LanguageDAOBuilder withDBService(IDBService dbService) {
        this.dbService = dbService
        return this
    }

    @Override
    LanguageDAOBuilder withConnection(IConnection connection) {
        this.connection = connection
        return this
    }

    @Override
    ILanguageDAO build() {
        return new LanguageDAO(this.dbService, this.connection)
    }

}