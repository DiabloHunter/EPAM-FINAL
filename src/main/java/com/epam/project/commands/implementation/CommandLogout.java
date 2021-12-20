package com.epam.project.commands.implementation;

import com.epam.project.commands.ICommand;
import com.epam.project.config.Configuration;
import com.epam.project.controller.Direction;
import com.epam.project.controller.ExecutionResult;
import com.epam.project.controller.SessionRequestContent;
import com.epam.project.exceptions.TransactionServiceException;
import com.epam.project.service.IInvoiceServ;
import com.epam.project.service.ITransactionServ;
import com.epam.project.service.ServiceFactory;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class CommandLogout implements ICommand {

    private static final Logger log = Logger.getLogger(CommandLogout.class);

    @Override
    public ExecutionResult execute(SessionRequestContent content) {

        Configuration conf = Configuration.getInstance();
        ExecutionResult result = new ExecutionResult();
        result.setDirection(Direction.REDIRECT);
        Double cash = Double.parseDouble(content.getRequestParameter("cash")[0]);
        Double cashBeforeWork =(Double) content.getSessionAttribute("cashBeforeWork");
        Timestamp timeBeforeWork = (Timestamp) content.getSessionAttribute("timeBeforeWork");
        ITransactionServ transactionServ= ServiceFactory.getTransactionService();
       /* try {
            String res = transactionServ.findAllTransactionsByTime(timeBeforeWork.toString());
            Double sum = 0.0;
            if(res!=null){
                sum = Double.parseDouble(res);
            }
            *//*Double sum = Double.parseDouble(transactionServ.findAllTransactionsByTime(timeBeforeWork.toString()));*//*
            if(sum+cashBeforeWork!=cash){
                result.addSessionAttribute("finishWork", false);
                result.addRequestAttribute("errorMessage", conf.getErrorMessage("moneyError"));
                result.setPage(conf.getPage("error"));
            }else{
                result.invalidateSession();
                result.setPage("/project");
            }
        } catch (TransactionServiceException e) {
            log.error(e);
            result.addRequestAttribute("errorMessage", conf.getErrorMessage("generalErr"));
            result.setPage(conf.getPage("error"));
        }*/
        try {
            result.invalidateSession();
            result.setPage("/project");
        }
        catch (Exception uue) {
            log.error(uue);
            result.addRequestAttribute("errorMessage", conf.getErrorMessage("generalErr"));
            result.setPage(conf.getPage("error"));
        }
        return result;
    }
}
