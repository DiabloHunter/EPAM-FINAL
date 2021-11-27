package com.epam.project.commands.implementation;

import com.epam.project.commands.ICommand;
import com.epam.project.config.Configuration;
import com.epam.project.controller.Direction;
import com.epam.project.controller.ExecutionResult;
import com.epam.project.controller.SessionRequestContent;
import com.epam.project.domain.Product;
import com.epam.project.domain.User;
import com.epam.project.service.IProductServ;
import com.epam.project.service.IUserServ;
import com.epam.project.service.ServiceFactory;
import org.apache.log4j.Logger;

public class CommandOpenUserDetails implements ICommand {

    private static final Logger log = Logger.getLogger(CommandOpenUserDetails.class);

    @Override
    public ExecutionResult execute(SessionRequestContent content) {
        Configuration config = Configuration.getInstance();
        ExecutionResult result = new ExecutionResult();
        result.setDirection(Direction.FORWARD);
        try {
            IUserServ serv = ServiceFactory.getUserService();
            String code = content.getRequestParameter("userId")[0];
            User user = serv.findUserById(Integer.parseInt(code));
            result.addRequestAttribute("user", user);
            result.setPage(config.getPage("userDetails"));
        } catch (Exception e) {
            log.error(e);
            result.addRequestAttribute("errorMessage", config.getErrorMessage("editUserPageErr"));
            result.setPage(config.getPage("error"));
        }
        return result;
    }
}
