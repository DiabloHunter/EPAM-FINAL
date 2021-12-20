package com.epam.project.commands.implementation;

import com.epam.project.commands.ICommand;
import com.epam.project.config.Configuration;
import com.epam.project.controller.Direction;
import com.epam.project.controller.ExecutionResult;
import com.epam.project.controller.SessionRequestContent;
import org.apache.log4j.Logger;

public class CommandGetCashAfterWork implements ICommand {
    private static final Logger log = Logger.getLogger(CommandValidateUser.class);

    @Override
    public ExecutionResult execute(SessionRequestContent content) {

        Configuration conf = Configuration.getInstance();
        ExecutionResult result = new ExecutionResult();
        result.setDirection(Direction.FORWARD);
        result.addSessionAttribute("finishWork", true);
        result.setPage(conf.getPage("redirect_home"));
        return result;
    }
}
