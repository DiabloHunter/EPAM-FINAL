package com.epam.project.commands.implementation;

import com.epam.project.commands.ICommand;
import com.epam.project.config.Configuration;
import com.epam.project.controller.Direction;
import com.epam.project.controller.ExecutionResult;
import com.epam.project.controller.SessionRequestContent;
import com.epam.project.domain.Product;
import com.epam.project.domain.User;
import com.epam.project.domain.UserCart;
import com.epam.project.exceptions.ProductServiceException;
import com.epam.project.exceptions.UnknownUserException;
import com.epam.project.service.IProductServ;
import com.epam.project.service.ServiceFactory;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class CommandGetCashBeforeWork implements ICommand {

    private static final Logger log = Logger.getLogger(CommandValidateUser.class);

    @Override
    public ExecutionResult execute(SessionRequestContent content) {
        Configuration conf = Configuration.getInstance();
        ExecutionResult result = new ExecutionResult();
        result.setDirection(Direction.FORWARD);
        String cash = content.getRequestParameter("cash")[0];
        String getCashBeforeWork = content.getRequestParameter("getCashBeforeWork")[0];
        result.addSessionAttribute("cashBeforeWork", Double.parseDouble(cash));
        result.addSessionAttribute("timeBeforeWork", new Timestamp(System.currentTimeMillis()));
        result.setPage(conf.getPage("redirect_home"));
        if(getCashBeforeWork.equals("true")){
            result.addSessionAttribute("getCashBeforeWork", true);
        }
        else{
            result.addSessionAttribute("getCashBeforeWork", false);
        }
        return result;
    }
}
