package com.linketinder.dao.candidatedao

import com.linketinder.database.DatabaseFactory
import com.linketinder.database.DBService
import com.linketinder.model.candidate.AcademicExperience
import com.linketinder.model.candidate.Candidate
import com.linketinder.model.candidate.Certificate
import com.linketinder.model.candidate.WorkExperience
import com.linketinder.model.candidate.Language
import com.linketinder.model.shared.Person
import com.linketinder.model.shared.Skill
import com.linketinder.model.shared.State
import com.linketinder.util.ErrorMessages
import com.linketinder.util.NotFoundMessages
import groovy.sql.Sql
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.logging.Level
import java.util.logging.Logger

class CandidateDAO {

    private final String QUERY_GET_ALL_CANDIDATES = "SELECT c.id, c.name, c.email, c.city, s.acronym AS state, c.country, c.cep, c.description, c.cpf FROM candidates AS c, states AS s WHERE c.state_id = s.id ORDER BY c.id"
    private final String QUERY_GET_CANDIDATE_BY_ID = "SELECT c.id, c.name, c.email, c.city, s.acronym AS state, c.country, c.cep, c.description, c.cpf FROM candidates AS c, states AS s WHERE c.state_id = s.id AND c.id=?"
    private final String QUERY_CERTIFICATE_ID_BY_CANDIDATE_ID = "SELECT id FROM certificates WHERE candidate_id=?"
    private final String QUERY_LANGUAGE_ID_BY_CANDIDATE_ID = "SELECT id FROM candidate_languages WHERE candidate_id=?"
    private final String QUERY_SKILL_ID_BY_CANDIDATE_ID = "SELECT id FROM candidate_skills WHERE candidate_id=?"
    private final String QUERY_GET_ACADEMIC_EXPERIENCE_ID_BY_CANDIDATE_ID = "SELECT id FROM academic_experiences WHERE candidate_id=?"
    private final String QUERY_GET_WORK_EXPERIENCE_ID_BY_CANDIDATE_ID = "SELECT id FROM work_experiences WHERE candidate_id=?"
    private final String INSERT_CANDIDATE = "INSERT INTO candidates (name, email, city, state_id, country, cep, description, cpf) VALUES (?,?,?,?,?,?,?,?)"
    private final String UPDATE_CANDIDATE = "UPDATE candidates SET name=?, email=?, city=?, state_id=?, country=?, cep=?, description=?, cpf=? WHERE id=?"
    private final String DELETE_CANDIDATE = "DELETE FROM candidates WHERE id=?"

    Sql sql = DatabaseFactory.instance()

    DBService dbService = new DBService()
    CertificateDAO certificateDAO = new CertificateDAO()
    LanguageDAO languageDAO = new LanguageDAO()
    CandidateSkillDAO skillDAO = new CandidateSkillDAO()
    AcademicExperienceDAO academicExperienceDAO = new AcademicExperienceDAO()
    WorkExperienceDAO workExperienceDAO = new WorkExperienceDAO()

    private List<Candidate> populateCandidates(String query) {
        List<Candidate> candidates = new ArrayList<>()
        PreparedStatement stmt = sql.connection.prepareStatement(query)
        ResultSet result = stmt.executeQuery()
        while (result.next()) {
            Person candidate = new Candidate()
            int candidateId = result.getInt("id")
            candidate.setId(candidateId)
            candidate.setName(result.getString("name"))
            candidate.setEmail(result.getString("email"))
            candidate.setCity(result.getString("city"))
            candidate.setState(State.valueOf(result.getString("state")))
            candidate.setCountry(result.getString("country"))
            candidate.setCep(result.getString("cep"))
            candidate.setDescription(result.getString("description"))
            candidate.setCpf(result.getString("cpf"))
            candidate.setCertificates(certificateDAO.getCertificatesByCandidateId(candidateId))
            candidate.setLanguages(languageDAO.getLanguagesByCandidateId(candidateId))
            candidate.setSkills(skillDAO.getSkillsByCandidateId(candidateId))
            candidate.setAcademicExperiences(academicExperienceDAO.getAcademicExperiencesByCandidateId(candidateId))
            candidate.setWorkExperiences(workExperienceDAO.getWorkExperiencesByCandidateId(candidateId))
            candidates.add(candidate)
        }
        return candidates
    }

    List<Candidate> getAllCandidates() {
        List<Candidate> candidates = new ArrayList<>()
        try {
            candidates = populateCandidates(QUERY_GET_ALL_CANDIDATES)
        } catch (SQLException e) {
            Logger.getLogger(DatabaseFactory.class.getName()).log(Level.SEVERE, ErrorMessages.DBMSG, e)
        }
        return candidates
    }

    private Candidate populateCandidate(String query, int id) {
        Person candidate = new Candidate()
        PreparedStatement stmt = sql.connection.prepareStatement(query)
        stmt.setInt(1, id)
        ResultSet result = stmt.executeQuery()
        while (result.next()) {
            candidate.setId(result.getInt("id"))
            candidate.setName(result.getString("name"))
            candidate.setEmail(result.getString("email"))
            candidate.setCity(result.getString("city"))
            candidate.setState(State.valueOf(result.getString("state")))
            candidate.setCountry(result.getString("country"))
            candidate.setCep(result.getString("cep"))
            candidate.setDescription(result.getString("description"))
            candidate.setCpf(result.getString("cpf"))
            candidate.setCertificates(certificateDAO.getCertificatesByCandidateId(candidate.id))
            candidate.setLanguages(languageDAO.getLanguagesByCandidateId(candidate.id))
            candidate.setSkills(skillDAO.getSkillsByCandidateId(candidate.id))
            candidate.setAcademicExperiences(academicExperienceDAO.getAcademicExperiencesByCandidateId(candidate.id))
            candidate.setWorkExperiences(workExperienceDAO.getWorkExperiencesByCandidateId(candidate.id))
        }
        return candidate
    }

    Candidate getCandidateById(int id) {
        Person candidate = new Candidate()
        try {
            candidate = populateCandidate(QUERY_GET_CANDIDATE_BY_ID, id)
        } catch (SQLException e) {
            Logger.getLogger(DatabaseFactory.class.getName()).log(Level.SEVERE, ErrorMessages.DBMSG, e)
        }
        return candidate
    }

    private void insertCertificates(Candidate candidate) {
        for (Certificate certificate in candidate.certificates) {
            certificateDAO.insertCertificate(certificate, candidate.id)
        }
    }

    private void insertLanguages(Candidate candidate) {
        for (Language language in candidate.languages) {
            languageDAO.insertLanguage(language, candidate.id)
        }
    }

    private void insertSkills(Candidate candidate) {
        for (Skill skill in candidate.skills) {
            skillDAO.insertSkill(skill, candidate.id)
        }
    }

    private void insertAcademicExperiences(Candidate candidate) {
        for (AcademicExperience academicExperience in candidate.academicExperiences) {
            academicExperienceDAO.insertAcademicExperience(academicExperience, candidate.id)
        }
    }

    private void insertWorkExperiences(Candidate candidate) {
        for (WorkExperience workExperience in candidate.workExperiences) {
            workExperienceDAO.insertWorkExperience(workExperience, candidate.id)
        }
    }

    void insertCandidate(Candidate candidate) {
        try {
            PreparedStatement stmt = sql.connection.prepareStatement(INSERT_CANDIDATE, Statement.RETURN_GENERATED_KEYS)
            stmt.setString(1, candidate.name)
            stmt.setString(2, candidate.getEmail())
            stmt.setString(3, candidate.getCity())
            stmt.setString(5, candidate.getCountry())
            stmt.setString(6, candidate.getCep())
            stmt.setString(7, candidate.getDescription())
            stmt.setString(8, candidate.getCpf())

            int stateId = dbService.idFinder("states", "acronym", candidate.getState().toString())
            stmt.setInt(4, stateId)

            stmt.executeUpdate()

            ResultSet getCandidateId = stmt.getGeneratedKeys()
            while (stmt.getGeneratedKeys().next()) {
                candidate.id = getCandidateId.getInt(1)
            }

            insertCertificates(candidate)
            insertLanguages(candidate)
            insertSkills(candidate)
            insertAcademicExperiences(candidate)
            insertWorkExperiences(candidate)
        } catch (SQLException e) {
            Logger.getLogger(DatabaseFactory.class.getName()).log(Level.SEVERE, ErrorMessages.DBMSG, e)
        }
    }

    private void updateCandidateCertificates(int id, Candidate candidate) {
        List<Integer> certificatesIds = new ArrayList<>()
        sql.eachRow(QUERY_CERTIFICATE_ID_BY_CANDIDATE_ID, [id]) {row ->
            certificatesIds << row.getInt("id")
        }

        List<Integer> persistedCertificates = certificatesIds.findAll { !candidate.certificates.contains(it) }
        persistedCertificates.each {certificatesId ->
            certificateDAO.deleteCertificate(certificatesId)
        }

        candidate.certificates.each {certificate ->
            if (persistedCertificates.contains(certificate.id)) {
                certificateDAO.updateCertificate(certificate, id)
            } else {
                certificateDAO.insertCertificate(certificate, id)
            }
        }
    }

    private void updateCandidateLanguages(int id, Candidate candidate) {
        List<Integer> languagesIds = new ArrayList<>()
        sql.eachRow(QUERY_LANGUAGE_ID_BY_CANDIDATE_ID, [id]) {row ->
            languagesIds << row.getInt("id")
        }

        List<Integer> persistedLanguages = languagesIds.findAll { !candidate.languages.contains(it) }
        persistedLanguages.each {languagesId ->
            languageDAO.deleteLanguage(languagesId)
        }

        candidate.languages.each {language ->
            if (persistedLanguages.contains(language.id)) {
                languageDAO.updateLanguage(language, id)
            } else {
                languageDAO.insertLanguage(language, id)
            }
        }
    }

    private void updateCandidateSkills(int id, Candidate candidate) {
        List<Integer> skillsIds = new ArrayList<>()
        sql.eachRow(QUERY_SKILL_ID_BY_CANDIDATE_ID, [id]) {row ->
            skillsIds << row.getInt("id")
        }

        List<Integer> persistedSkills = skillsIds.findAll { !candidate.skills.contains(it) }
        persistedSkills.each {skillsId ->
            skillDAO.deleteSkill(skillsId)
        }

        candidate.skills.each {skill ->
            if (persistedSkills.contains(skill.id)) {
                skillDAO.updateSkill(skill, id)
            } else {
                skillDAO.insertSkill(skill, id)
            }
        }
    }

    private void updateCandidateAcademicExperiences(int id, Candidate candidate) {
        List<Integer> academicExperiencesIds = new ArrayList<>()
        sql.eachRow(QUERY_GET_ACADEMIC_EXPERIENCE_ID_BY_CANDIDATE_ID, [id]) {row ->
            academicExperiencesIds << row.getInt("id")
        }

        List<Integer> persistedAcademicExperiences = academicExperiencesIds.findAll {
            !candidate.academicExperiences.contains(it) }
        persistedAcademicExperiences.each {academicExperiencesId ->
            academicExperienceDAO.deleteAcademicExperience(academicExperiencesId)
        }

        candidate.academicExperiences.each {academicExperience ->
            if (persistedAcademicExperiences.contains(academicExperience.id)) {
                academicExperienceDAO.updateAcademicExperience(academicExperience, id)
            } else {
                academicExperienceDAO.insertAcademicExperience(academicExperience, id)
            }
        }
    }

    private void updateCandidateWorkExperiences(int id, Candidate candidate) {
        List<Integer> workExperiencesIds = new ArrayList<>()
        sql.eachRow(QUERY_GET_WORK_EXPERIENCE_ID_BY_CANDIDATE_ID, [id]) {row ->
            workExperiencesIds << row.getInt("id")
        }

        List<Integer> persistedWorkExperiences = workExperiencesIds.findAll { !candidate.workExperiences.contains(it) }
        persistedWorkExperiences.each {workExperiencesId ->
            workExperienceDAO.deleteWorkExperience(workExperiencesId)
        }

        candidate.workExperiences.each {workExperience ->
            if (persistedWorkExperiences.contains(workExperience.id)) {
                workExperienceDAO.updateWorkExperience(workExperience, id)
            } else {
                workExperienceDAO.insertWorkExperience(workExperience, id)
            }
        }
    }

    void updateCandidate(int id, Candidate candidate) {
        try {
            PreparedStatement stmt = sql.connection.prepareStatement(UPDATE_CANDIDATE)
            stmt.setString(1, candidate.name)
            stmt.setString(2, candidate.getEmail())
            stmt.setString(3, candidate.getCity())
            stmt.setString(5, candidate.getCountry())
            stmt.setString(6, candidate.getCep())
            stmt.setString(7, candidate.getDescription())
            stmt.setString(8, candidate.getCpf())
            stmt.setInt(9, id)

            int stateId = dbService.idFinder("states", "acronym", candidate.getState().toString())
            stmt.setInt(4, stateId)

            stmt.executeUpdate()

            updateCandidateCertificates(id, candidate)
            updateCandidateLanguages(id, candidate)
            updateCandidateSkills(id, candidate)
            updateCandidateAcademicExperiences(id, candidate)
            updateCandidateWorkExperiences(id, candidate)
        } catch (SQLException e) {
            Logger.getLogger(DatabaseFactory.class.getName()).log(Level.SEVERE, ErrorMessages.DBMSG, e)
        }
    }

    void deleteCandidateById(int id) {
        Person candidate = new Candidate()
        try {
            PreparedStatement stmt = sql.connection.prepareStatement(QUERY_GET_CANDIDATE_BY_ID)
            stmt.setInt(1, id)
            ResultSet result = stmt.executeQuery()
            while (result.next()) {
                candidate.setId(result.getInt("id"))
            }

            if (candidate.id != null) {
                stmt = sql.connection.prepareStatement(DELETE_CANDIDATE)
                stmt.setInt(1, id)
                stmt.executeUpdate()
                println "Candidato Removido"
                return
            }
        } catch (SQLException e) {
            Logger.getLogger(DatabaseFactory.class.getName()).log(Level.SEVERE, ErrorMessages.DBMSG, e)
        }
        println NotFoundMessages.CANDIDATE
    }

}
