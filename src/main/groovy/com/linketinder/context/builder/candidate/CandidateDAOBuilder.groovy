package com.linketinder.context.builder.candidate

import com.linketinder.context.builder.interfaces.*
import com.linketinder.dao.candidatedao.*
import com.linketinder.dao.candidatedao.interfaces.*
import com.linketinder.database.interfaces.*

class CandidateDAOBuilder implements ICandidateDAOBuilder {

    IConnection connection
    IDBService dbService

    IAcademicExperienceDAO academicExperienceDAO
    ICandidateSkillDAO candidateSkillDAO
    ICertificateDAO certificateDAO
    ILanguageDAO languageDAO
    IWorkExperienceDAO workExperienceDAO

    @Override
    CandidateDAOBuilder withDBService(IDBService dbService) {
        this.dbService = dbService
        return this
    }

    @Override
    CandidateDAOBuilder withConnection(IConnection connection) {
        this.connection = connection
        return this
    }

    @Override
    CandidateDAOBuilder withAcademicExperienceDAO(IAcademicExperienceDAO academicExperienceDAO) {
        this.academicExperienceDAO = academicExperienceDAO
        return this
    }

    @Override
    CandidateDAOBuilder withCandidateSkillDAO(ICandidateSkillDAO candidateSkillDAO) {
        this.candidateSkillDAO = candidateSkillDAO
        return this
    }

    @Override
    CandidateDAOBuilder withCertificateDAO(ICertificateDAO certificateDAO) {
        this.certificateDAO = certificateDAO
        return this
    }

    @Override
    CandidateDAOBuilder withLanguageDAO(ILanguageDAO languageDAO) {
        this.languageDAO = languageDAO
        return this
    }

    @Override
    CandidateDAOBuilder withWorkExperienceDAO(IWorkExperienceDAO workExperienceDAO) {
        this.workExperienceDAO = workExperienceDAO
        return this
    }

    @Override
    ICandidateDAO build() {
        return new CandidateDAO(
            this.dbService,
            this.connection,
            this.certificateDAO,
            this.languageDAO,
            this.candidateSkillDAO,
            this.academicExperienceDAO,
            this.workExperienceDAO
        )
    }

}
