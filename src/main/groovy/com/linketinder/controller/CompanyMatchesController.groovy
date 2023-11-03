package com.linketinder.controller

import com.linketinder.service.factories.MatchServiceFactory
import com.linketinder.service.interfaces.IMatchService
import com.linketinder.util.*
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.logging.Level
import java.util.logging.Logger

@WebServlet(urlPatterns = ["/companymatches", "/companymatches/*"], asyncSupported = true)
class CompanyMatchesController extends HttpServlet {

    IMatchService service

    void init () {
        this.service = MatchServiceFactory.createMatchService()
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            this.service.getAllMatchesByCompanyId(req, resp)
        } catch (ServletException | IOException e) {
            Logger.getLogger(CompanyMatchesController.class.getName()).log(Level.INFO, ErrorMessages.CTLR_TEXT, e)
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST)
            return
        }
        resp.setStatus(HttpServletResponse.SC_OK)
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            this.service.likeCandidate(req, resp)
        } catch (ServletException | IOException e) {
            Logger.getLogger(CompanyMatchesController.class.getName()).log(Level.INFO, ErrorMessages.CTLR_TEXT, e)
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST)
            return
        }
        resp.setStatus(HttpServletResponse.SC_CREATED)
    }

}
